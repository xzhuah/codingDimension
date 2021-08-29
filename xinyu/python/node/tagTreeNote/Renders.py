# Created by Xinyu Zhu on 2021/8/29, 1:48
from node.tagTreeNote.File import File, FileCollection


class Render:
    def __init__(self):
        pass

    # return a str in specific format (markdown/html/...) to render a File object, ignore the path of file
    def render_file(self, file: File) -> str:
        raise NotImplementedError

    # return a str in specific format (markdown/html/...) to render a FileCollection object
    def render_file_collection(self, file_collection:FileCollection) -> str:
        raise NotImplementedError
