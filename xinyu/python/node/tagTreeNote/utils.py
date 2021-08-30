# Created by Xinyu Zhu on 2021/8/29, 3:40
from node.tagTreeNote.Config import *


def verify_str_with_illegal_char(text: str, illegal_chars: set) -> bool:
    for c in text:
        if c in illegal_chars:
            return False
    return True


def verify_folder(folder: str) -> bool:
    return verify_str_with_illegal_char(folder, ILLEGAL_CHARS_FOR_FOLDER_NAME)


def verify_filename(filename: str) -> bool:
    return verify_str_with_illegal_char(filename, ILLEGAL_CHARS_FOR_FILENAME)


def verify_uri(uri: str) -> bool:
    return verify_str_with_illegal_char(uri, ILLEGAL_CHARS_FOR_URI)


def verify_tag(tag: str) -> bool:
    return verify_str_with_illegal_char(tag, ILLEGAL_CHARS_FOR_TAG)
