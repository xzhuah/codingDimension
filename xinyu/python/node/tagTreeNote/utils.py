# Created by Xinyu Zhu on 2021/8/29, 3:40
from common.tools.utils import check_state
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


def common_heading_sub_array(arr1: list, arr2: list) -> list:
    shared_parent = []
    for i in range(min(len(arr1), len(arr2))):
        if arr1[i] == arr2[i]:
            shared_parent.append(arr1[i])
        else:
            break
    return shared_parent


class TagsFilenameListSortHelper:
    def __init__(self, tags_filename: list):
        # [tag1, tag2, ..., filename]
        check_state(len(tags_filename) > 0)
        self.tags_filename = tags_filename

    def __str__(self):
        return str(self.tags_filename)

    def __repr__(self):
        return str(self.tags_filename)

    def __lt__(self, other):
        for i in range(min(len(self.tags_filename), len(other.tags_filename)) - 1):
            if self.tags_filename[i] != other.tags_filename[i]:
                return self.tags_filename[i] < other.tags_filename[i]
        if len(self.tags_filename) == len(other.tags_filename):
            return self.tags_filename[-1] < other.tags_filename[-1]
        # shorter is smaller
        return len(self.tags_filename) < len(other.tags_filename)

    def __gt__(self, other):
        for i in range(min(len(self.tags_filename), len(other.tags_filename)) - 1):
            if self.tags_filename[i] != other.tags_filename[i]:
                return self.tags_filename[i] > other.tags_filename[i]
        if len(self.tags_filename) == len(other.tags_filename):
            return self.tags_filename[-1] > other.tags_filename[-1]
        # longer is larger
        return len(self.tags_filename) > len(other.tags_filename)

    def __eq__(self, other):
        if len(self.tags_filename) != len(other.tags_filename):
            return False
        for i in range(len(self.tags_filename)):
            if self.tags_filename[i] != other.tags_filename[i]:
                return False
        return True


if __name__ == '__main__':
    t1 = TagsFilenameListSortHelper(['bad', 'BB'])
    t2 = TagsFilenameListSortHelper(['bad', '2', 'B'])
    assert t1 < t2
