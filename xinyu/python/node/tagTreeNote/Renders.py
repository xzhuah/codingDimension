# Created by Xinyu Zhu on 2021/8/29, 1:48
from node.tagTreeNote.File import File, FileCollection
from node.tagTreeNote.Path import Path
from common.tools.utils import check_state


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
        check_state(tag_list_2d is list)
        for ele in tag_list_2d:
            sub_list = []
            check_state(ele is list)
            for tag in ele:
                check_state(tag is list)
                tag = tag.strip()
                check_state(len(tag) > 0 and tag not in check_set)
                check_set.add(tag)
                sub_list.append(tag)
            if len(sub_list) > 0:
                validated_list.append(sub_list)


class MarkdownRender(Render):
    def render_file(self, file: File) -> str:
        return "[{title}]({uri}) <!-- {tag_list} --> <!-- {metadata} -->".format(title=file.name, uri=file.uri,
                                                                         tag_list=MarkdownRender.render_tags(file.tags),
                                                                         metadata=file.metadata)

    def render_file_collection(self, file_collection: FileCollection, tag_list_2d=None) -> str:
        return ""

    @staticmethod
    def render_tags(tags: set) -> str:
        return ", ".join(tags)


if __name__ == '__main__':
    file1 = File("A", "a.c.d", Path("a/c/d"))
    file1.add_tag("good").add_tag("middle")
    file2 = File("B", "a.b.e", Path("a/b/e"))
    file2.add_tags({"bad", "middle"})

    file_col = FileCollection()
    file_col.add_files([file1, file2])

    render = MarkdownRender()
    print(render.render_file(file1))
    print(render.render_file(file2))
    print(render.render_file_collection(file_col))
