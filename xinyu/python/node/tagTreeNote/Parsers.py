# Created by Xinyu Zhu on 2021/8/29, 1:53
from node.tagTreeNote.File import File, FileCollection
from node.tagTreeNote.Path import Path


class Parser:
    def __init__(self):
        pass

    # parse from a str in specific format (markdown/html/...) to a File object, ignore the path of file
    def parse_file(self, content: str) -> File:
        raise NotImplementedError

    # parse from a str in specific format (markdown/html/...) to a FileCollection object
    def render_file_collection(self, content: str) -> FileCollection:
        raise NotImplementedError
