# Created by Xinyu Zhu on 2021/8/29, 1:53
from node.tagTreeNote.models.File import File, FileCollection
from node.tagTreeNote.models.Path import Path
from node.tagTreeNote.renders.Renders import MarkdownRender
from common.tools.utils import check_state
import re
import json


class Parser:
    def __init__(self):
        pass

    # parse from a str in specific format (markdown/html/...) to a File object, ignore the path of file
    def parse_file(self, content: str) -> File:
        raise NotImplementedError

    # parse from a str in specific format (markdown/html/...) to a FileCollection object
    def parse_file_collection(self, content: str) -> FileCollection:
        raise NotImplementedError


class MarkdownParser(Parser):
    line_break = "\n"
    file_line_pattern = re.compile(r"\[([^\]]+)\]\(([^)]*)\) <!\-\-([^<]*)\-\-> <!\-\-([^<]+)\-\->")
    # using \t as default tab str
    folder_line_pattern = re.compile(r"([\t]*)\* ([^\[]+)")
    file_line_without_metadata_pattern = re.compile(r"\[([^\]]+)\]\(([^)]*)\) <!\-\-([^<]*)\-\->")
    file_line_without_tags_or_metadata_pattern = re.compile(r"\[([^\]]+)\]\(([^)]*)\)")

    def __init__(self):
        super().__init__()

    def parse_file(self, content: str) -> File:
        match_result = MarkdownParser.file_line_pattern.match(content)
        check_state(match_result is not None)
        filename = match_result.group(1)
        uri = match_result.group(2)
        tags = self.parse_tag_str(match_result.group(3))
        metadata = json.loads(match_result.group(4))
        file = File(filename, uri, Path(metadata["path"]))
        file.metadata = metadata
        file.add_tags(tags)
        return file

    def parse_file_collection(self, content: str) -> FileCollection:
        file_col = FileCollection()
        all_file = MarkdownParser.file_line_pattern.finditer(content)
        for file in all_file:
            file_col.add_file(self.parse_file(file.group()))
        return file_col

    def parse_file_collection_without_metadata_or_tags(self, content: str) -> FileCollection:
        file_col = FileCollection()

        pre_path = Path()
        for line in content.split(MarkdownParser.line_break):
            if "[" in line:
                match_result = MarkdownParser.file_line_without_metadata_pattern.search(line)
                if match_result is not None:
                    filename = match_result.group(1)
                    uri = match_result.group(2)
                    tags = self.parse_tag_str(match_result.group(3))
                    file = File(filename, uri, pre_path)
                    file.add_tags(tags)
                else:
                    match_result = MarkdownParser.file_line_without_tags_or_metadata_pattern.search(line)
                    check_state(match_result is not None)
                    filename = match_result.group(1)
                    uri = match_result.group(2)
                    file = File(filename, uri, pre_path)
                file_col.add_file(file)
            else:
                match_result = MarkdownParser.folder_line_pattern.match(line)
                if match_result is None:
                    continue
                folder_level = match_result.group(1).count("\t") + 1
                folder_name = match_result.group(2)
                if folder_level == pre_path.depth():
                    cur_path = pre_path.sibling(folder_name)
                elif folder_level > pre_path.depth():
                    cur_path = pre_path.child(folder_name)
                else:
                    diff = pre_path.depth() - folder_level
                    cur_path = pre_path.parent_(diff).sibling(folder_name)
                pre_path = cur_path
        return file_col

    @staticmethod
    def parse_tag_str(tags: str) -> list:
        return tags.strip().split(",")


if __name__ == '__main__':
    file1 = File("A", "a.c.d", Path("a/c/d"))
    file1.add_tag("good").add_tag("middle")

    render = MarkdownRender()
    obj_str = render.render_file(file1)
    print(obj_str)
    parser = MarkdownParser()
    file2 = parser.parse_file(obj_str)
    print(file2)
    obj_str2 = render.render_file(file2)
    print(obj_str2)
    print(obj_str2 == obj_str)
