# Created by Xinyu Zhu on 2021/8/29, 1:48
from node.tagTreeNote.models.File import File, FileCollection
from node.tagTreeNote.models.Path import Path
from common.tools.utils import check_state
from node.tagTreeNote.utils.utils import common_heading_sub_array, TagsFilenameListSortHelper
import json
import os


class Render:
    def __init__(self):
        pass

    # return a str in specific format (markdown/html/...) to render a File object, ignore the path of file
    def render_file(self, file: File) -> str:
        raise NotImplementedError

    # return a str in specific format (markdown/html/...) to render a FileCollection object
    # use the path to organize the tree when tag_list_2d is None
    # otherwise organize the tree with tag_list_2d
    def render_file_collection(self, file_collection: FileCollection, tag_list_2d=None) -> str:
        raise NotImplementedError

    @staticmethod
    def validate_tag_list_2d(tag_list_2d=None) -> list:
        if tag_list_2d is None:
            return None
        check_set = set()
        validated_list = []
        check_state(isinstance(tag_list_2d[0], list))
        for ele in tag_list_2d:
            sub_list = []
            check_state(isinstance(ele, list))
            for tag in ele:
                check_state(isinstance(tag, str))
                tag = tag.strip()
                check_state(len(tag) > 0 and tag not in check_set)
                check_set.add(tag)
                sub_list.append(tag)
            if len(sub_list) > 0:
                validated_list.append(sub_list)
        return validated_list


class MarkdownRender(Render):
    tab_str = "\t"
    line_break = "\n"

    def render_file(self, file: File) -> str:
        return self.render_file_helper(file)

    # Parse only support parsing files with tags and metadata
    def render_file_helper(self, file: File, keep_tags=True, keep_metadata=True) -> str:
        if keep_tags and keep_metadata:
            template = "[{title}]({uri}) <!-- {tag_list} --> <!-- {metadata} -->"
        elif not keep_tags and keep_metadata:
            template = "[{title}]({uri}) <!-- {metadata} -->"
        elif keep_tags and not keep_metadata:
            template = "[{title}]({uri}) <!-- {tag_list} -->"
        else:
            template = "[{title}]({uri})"
        return template.format(title=file.name, uri=file.uri,
                               tag_list=MarkdownRender.render_tags(
                                   file.tags),
                               metadata=self.render_metadata(
                                   file.metadata))

    def render_file_collection(self, file_collection: FileCollection, tag_list_2d=None) -> str:
        return self.render_file_collection_helper(file_collection, tag_list_2d)

    def render_file_collection_helper(self, file_collection: FileCollection, tag_list_2d=None, keep_tags=True,
                                      keep_metadata=True) -> str:
        validated_tag_list = self.validate_tag_list_2d(tag_list_2d)
        result = ""
        if validated_tag_list is None:
            # render tree with path specified structure O(n * m)
            all_file = sorted(file_collection.collection)
            pre_path = Path()
            for file in all_file:
                cur_path = file.path
                common_parent = cur_path.common_path(pre_path)
                cur_path_array = cur_path.split()
                common_parent_array = common_parent.split()
                prefix_tab_num = len(common_parent_array)
                diff_array_need_to_render = cur_path_array[len(common_parent_array):]
                for i, folder in enumerate(diff_array_need_to_render):
                    result += (prefix_tab_num + i) * MarkdownRender.tab_str + "* " + folder + MarkdownRender.line_break
                result += len(cur_path_array) * MarkdownRender.tab_str + "* " + self.render_file_helper(
                    file=file, keep_tags=keep_tags, keep_metadata=keep_metadata) + MarkdownRender.line_break
                pre_path = cur_path
            return result
        else:
            all_tag_to_level = {}
            for i, tag_list in enumerate(validated_tag_list):
                for tag in tag_list:
                    all_tag_to_level[tag] = i
            all_file = file_collection.collection
            sorted_file = MarkdownRender.sort_files_by_tags(all_file, validated_tag_list)
            pre_tags = []
            for file in sorted_file:
                cur_tags = MarkdownRender.tag_list_ordered_by_level(file.tags, all_tag_to_level)
                if cur_tags is None:
                    continue
                common_parent_array = common_heading_sub_array(cur_tags, pre_tags)
                prefix_tab_num = len(common_parent_array)
                diff_array_need_to_render = cur_tags[len(common_parent_array):]
                for i, tag in enumerate(diff_array_need_to_render):
                    result += (prefix_tab_num + i) * MarkdownRender.tab_str + "* " + tag + MarkdownRender.line_break
                result += len(cur_tags) * MarkdownRender.tab_str + "* " + self.render_file_helper(
                    file=file, keep_tags=keep_tags, keep_metadata=keep_metadata) + MarkdownRender.line_break
                pre_tags = cur_tags

            return result

    @staticmethod
    def sort_files_by_tags(files: list, validated_tag_list_2d: list) -> list:
        filtered_file_list = MarkdownRender.filter_file_by_tag_group(files, validated_tag_list_2d)
        result = sorted(filtered_file_list, key=lambda e: MarkdownRender.to_list_sort_helper(e, validated_tag_list_2d))
        return result

    @staticmethod
    def to_list_sort_helper(filtered_file: File, validated_tag_list_2d: list) -> TagsFilenameListSortHelper:
        tags = []
        for tag_list in validated_tag_list_2d:
            for tag in tag_list:
                if tag in filtered_file.tags:
                    tags.append(tag)
                    break
        tags.append(filtered_file.name)
        return TagsFilenameListSortHelper(tags)

    @staticmethod
    def filter_file_by_tag_group(files: list, validated_tag_list_2d: list) -> list:
        tag_to_level = {}
        for i, tag_list in enumerate(validated_tag_list_2d):
            for tag in tag_list:
                tag_to_level[tag] = i

        result = []
        for file in files:
            if MarkdownRender.file_has_valid_tags(file, tag_to_level):
                result.append(file)
        return result

    @staticmethod
    def file_has_valid_tags(file: File, tag_to_level: dict) -> bool:
        deepest_level = -1
        files_level_to_tag = {}
        for tag in file.tags:
            if tag in tag_to_level:
                level = tag_to_level[tag]
                if level in files_level_to_tag:
                    raise Exception("file's tags {tags} can't from the same group: {group}".format(tags=file.tags,
                                                                                                   group=[
                                                                                                       files_level_to_tag[
                                                                                                           level],
                                                                                                       tag]))
                files_level_to_tag[level] = tag
                if level > deepest_level:
                    deepest_level = level
        return len(files_level_to_tag) == deepest_level + 1 and len(files_level_to_tag) > 0

    @staticmethod
    def tag_list_ordered_by_level(tags: set, tag_to_level: dict) -> list:
        level_to_tag = {}
        max_level = -1
        for tag in tags:
            if tag in tag_to_level:
                level = tag_to_level[tag]
                if level > max_level:
                    max_level = level
                if level in level_to_tag:
                    raise Exception("file's tags {tags} can't from the same group: {group}".format(tags=tags,
                                                                                                   group=[level_to_tag[
                                                                                                              level],
                                                                                                          tag]))
                level_to_tag[level] = tag
        if max_level + 1 == len(level_to_tag):
            result = []
            for i in range(len(level_to_tag)):
                result.append(level_to_tag[i])
            return result
        else:
            # tags set need to be filtered out
            return None

    # render tree with tag list specified structure
    @staticmethod
    def render_tags(tags: set) -> str:
        return ", ".join(tags)

    @staticmethod
    def render_metadata(metadata: dict):
        result = json.dumps(metadata)
        check_state("<" not in result)

        return result


if __name__ == '__main__':
    file1 = File("A", "a.c.d", Path("a/c/d"))
    file1.add_tag("good").add_tag("1")

    file2 = File("B", "a.b.e", Path("a/b/e"))
    file2.add_tags({"bad", "2"})

    file3 = File("B", "a.b.e", Path("b/b/e"))
    file3.add_tags({"bad", "2"})

    file4 = File("BB", "a.b.e", Path("b/b/e"))
    file4.add_tags({"middle", "1"})

    file5 = File("BB", "a.b.e", Path("b/b/e/d"))
    file5.add_tags({"bad"})

    file6 = File("BB6", "a.b.e", Path("b/b/e/d"))
    file6.add_tags({"bad", "3"})

    file7 = File("BB7", "a.b.e", Path("b/b/e/d"))
    file7.add_tags({"7"})

    file8 = File("BB8", "a.b.e", Path("b/b/e/d"))
    file8.add_tags({"7", "middle"})

    file_col = FileCollection()
    file_col.add_files([file1, file2, file3, file4, file5, file6, file7, file8])

    render = MarkdownRender()
    # print(render.render_file(file1))
    # print(render.render_file_helper(file2, False, False))
    print(render.render_file_collection(file_col))
    # print(render.render_file_collection_helper(file_col, tag_list_2d=[["bad", "middle", "good"], ["1", "2", "7"]],
    #                                            keep_tags=True, keep_metadata=True))
