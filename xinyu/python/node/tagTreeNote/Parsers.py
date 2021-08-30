# Created by Xinyu Zhu on 2021/8/29, 1:53
from node.tagTreeNote.File import File, FileCollection
from node.tagTreeNote.Path import Path
from node.tagTreeNote.Renders import MarkdownRender
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
    def render_file_collection(self, content: str) -> FileCollection:
        raise NotImplementedError


class MarkdownParser(Parser):
    file_line_pattern = re.compile(r"\[([^\]]+)\]\(([^)]*)\) <!\-\-([^<]*)\-\-> <!\-\-([^<]+)\-\->")

    def __init__(self):
        super().__init__()

    def parse_file(self, content: str) -> File:
        match_result = MarkdownParser.file_line_pattern.match(content)
        check_state(match_result is not None)
        filename = match_result.group(1)
        uri = match_result.group(2)
        tags = self.parse_tag_str(match_result.group(3))
        print(match_result.group(4))
        metadata = json.loads(match_result.group(4))
        file = File(filename, uri, Path(metadata["path"]))
        file.metadata = metadata
        file.add_tags(tags)
        return file

    def render_file_collection(self, content: str) -> FileCollection:
        pass

    def parse_tag_str(self, tags: str) -> list:
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
