# Created by Xinyu Zhu on 2021/8/28, 23:59
from common.tools.utils import check_state
from node.tagTreeNote.utils import verify_folder, DEFAULT_PATH_SEPARATOR
import json

class Path:
    # Object should be immutable after init
    def __init__(self, path="", validate_folder_name=True):
        # "/" is reserved for the default separator.
        self.separator = DEFAULT_PATH_SEPARATOR
        # a string in the format of "a/b/c" to represent a path
        # "" can be used
        self.path = path.strip().strip(self.separator).strip()
        if validate_folder_name and self.separator in self.path:
            self._strip_folder_name()
        if validate_folder_name:
            for folder in self.split():
                check_state(verify_folder(folder))

    def _strip_folder_name(self):
        all_folder_in_order = self.path.split(self.separator)
        result = ""
        for folder_name in all_folder_in_order:
            folder_name = folder_name.strip()
            check_state(len(folder_name) > 0, self.path + "is invalid")
            result += folder_name + self.separator
        self.path = result[:-1]

    def get_path(self):
        return self.path

    # turn the path into a list of folder name
    def split(self) -> list:
        return self.path.split(self.separator) if len(self.path) > 0 else []

    # return depth of the path (number of folders along the way)
    def depth(self) -> int:
        return self.path.count(self.separator) + 1 if len(self.path) > 0 else 0

    def parent(self):
        if self.depth() <= 1:
            return Path()
        else:
            return Path(self.path[: self.path.rindex(self.separator)], validate_folder_name=False)

    def copy(self):
        return Path(self.path, validate_folder_name=False)

    def parent_or_self(self):
        if self.depth() <= 1:
            return self.copy()
        else:
            return Path(self.path[: self.path.rindex(self.separator)], validate_folder_name=False)

    def child(self, child_name: str):
        child_name = child_name.strip().strip(self.separator).strip()
        check_state(len(child_name) > 0, child_name + "is invalid")
        if len(self.path) == 0:
            return Path(child_name)
        return Path(self.path + self.separator + child_name)

    def sibling(self, sibling_name: str):
        sibling_name = sibling_name.strip().strip(self.separator).strip()
        if self.depth() <= 1:
            return Path(sibling_name)
        else:
            return Path(self.path[: self.path.rindex(self.separator)] + self.separator + sibling_name)

    def get_leaf(self) -> str:
        if self.separator in self.path:
            return self.path[self.path.rindex(self.separator) + len(self.separator)]
        else:
            return self.path

    def is_child_of(self, other_path) -> bool:
        check_state(self.separator == other_path.separator, "The separator used in two path are not the same")
        return self.path.startswith(other_path.path)

    def is_parent_of(self, other_path) -> bool:
        return other_path.is_child_of(self)

    def to_map(self) -> dict:
        all_folder = self.split()
        child = {}
        for i in range(len(all_folder) - 1, -1, -1):
            current = {all_folder[i]: child}
            child = current
        return child

    def __repr__(self):
        return self.path

    def __str__(self):
        return self.path

    def __lt__(self, other):
        a = self.split()
        b = other.split()
        l = min(self.depth(), other.depth())
        for i in range(l):
            if a[i] != b[i]:
                return a[i] < b[i]
        return self.depth() < other.depth()

    def __gt__(self, other):
        a = self.split()
        b = other.split()
        l = min(self.depth(), other.depth())
        for i in range(l):
            if a[i] != b[i]:
                return a[i] > b[i]
        return self.depth() > other.depth()

    def __eq__(self, other):
        return self.path == other.path


    def toJson(self):
        return json.dumps(self, default=lambda o: o.__dict__)


if __name__ == '__main__':
    path1 = Path("a/b/c")

    path2 = Path("b/c")
    path3 = Path("a/")
    path4 = Path(" / a/b")

    print(path1.is_child_of(path2))
    print(path1.is_child_of(path3))
    print(path1.is_child_of(path4))
    print(path1 > path4)

    t = path4.get_path()
    t = "a/c"
    print(path4)

    print(path4.to_map())
