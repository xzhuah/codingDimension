# Created by Xinyu Zhu on 2021/8/31, 21:59
from node.tagTreeNote.Config import DEFAULT_PATH_SEPARATOR
from node.tagTreeNote.models.File import FileCollection, File
from node.tagTreeNote.models.Path import Path
from node.tagTreeNote.parsers.Parsers import Parser
from pathlib import Path as SysPath
from urllib.parse import quote
from node.tagTreeNote.Config import ILLEGAL_CHARS_FOR_FILENAME
import os


class DiskFileParser(Parser):

    # parse a file on disk, give the path to the file
    def parse_file(self, content: str) -> File:
        filepath = SysPath(content)
        # by default, remove all illegal char automatically
        filename = DiskFileParser.remove_illegal_character_for_filename(filepath.name)
        parent = filepath.parent.absolute()
        path = Path(str(parent).replace(os.sep, DEFAULT_PATH_SEPARATOR))
        result = File(filename, DiskFileParser.to_path_url_(filepath.absolute()), path)
        return result

    # recursively parse all files under a directory and all its sub-directory
    def parse_file_collection(self, content: str) -> FileCollection:
        return self.parse_file_collection_helper(content)

    # recursively parse all files under a directory using a brand-width search algorithm, with a maximum search level
    # -1 means parse to the end, 0 means only parse the direct children of the given directory
    def parse_file_collection_helper(self, content: str, max_level=-1) -> FileCollection:
        content = DiskFileParser.simplify_path(str(SysPath(content).absolute()).replace(os.sep, DEFAULT_PATH_SEPARATOR))
        dir_path, dir_names, filenames = next(os.walk(content))
        if not dir_path.endswith(os.sep):
            dir_path += os.sep
        result = FileCollection()
        for filename in filenames:
            result.add_file(self.parse_file(dir_path + filename))
        if max_level > 0:
            for dir in dir_names:
                result.add_collection(self.parse_file_collection_helper(dir_path + dir, max_level - 1))
        elif max_level < 0:
            for dir in dir_names:
                result.add_collection(self.parse_file_collection_helper(dir_path + dir, -1))
        return result

    @staticmethod
    def remove_illegal_character_for_filename(filename: str) -> str:
        result = filename
        for char in ILLEGAL_CHARS_FOR_FILENAME:
            result = result.replace(char, "")
        return result

    @staticmethod
    def to_path_url(path_str: str) -> str:
        abs_path = str(SysPath(path_str).absolute())
        return DiskFileParser.normalize_abs_path_str(abs_path)

    @staticmethod
    def to_path_url_(path: SysPath) -> str:
        abs_path = str(path.absolute())
        return DiskFileParser.normalize_abs_path_str(abs_path)

    @staticmethod
    def normalize_abs_path_str(abs_path: str) -> str:
        path_str = str(abs_path.replace(os.sep, DEFAULT_PATH_SEPARATOR))
        if ":" in path_str:
            disk = path_str[:path_str.index(":") + 1]
            path_str = path_str[path_str.index(":") + 1:]
            return "file:///" + disk + quote(path_str)
        else:
            return "file:///" + (path_str)

    @staticmethod
    def simplify_path(a):
        st = [DEFAULT_PATH_SEPARATOR]
        b = ":" in a
        a = a.split(DEFAULT_PATH_SEPARATOR)
        for i in a:
            if i == '..':
                if len(st) > 1:
                    st.pop()
                else:
                    continue
            elif i == '.':
                continue
            elif i != '':
                st.append("/" + str(i))
        if len(st) == 1:
            return "/" if not b else ""
        return "".join(st[1:]) if not b else "".join(st[1:])[1:]


if __name__ == '__main__':
    test_folder = "C:/my C/Work/Work General/"
    test_file = test_folder + "/0.jpg"
    parser = DiskFileParser()
    # file = parser.parse_file(test_file)
    # print(file)

    result = parser.parse_file_collection(test_folder)

    print(result)
