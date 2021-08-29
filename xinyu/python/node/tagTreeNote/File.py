# Created by Xinyu Zhu on 2021/8/28, 23:55
from node.tagTreeNote.Path import Path
from common.tools.utils import check_state
from node.tagTreeNote.utils import verify_filename, verify_tag


class File:

    def __init__(self, filename: str, uri: str, path: Path):
        # name of file
        self.name = filename.strip()
        check_state(verify_filename(filename))
        # a string act as uri
        self.uri = uri
        # a set for holding customer specified tags (str)
        self.tags = set()
        # metadata for the file, free_formed, open for extension
        self.metadata = dict()
        # path of the file
        self.metadata["path"] = path

    def add_tag(self, tag: str):
        tag = tag.strip()
        # disallow { and } in tag to avoid
        check_state(len(tag) > 0 and verify_tag(tag), "tag is invalid")
        self.tags.add(tag)
        return self

    def add_tags(self, tags, need_verify=True):
        if need_verify:
            for tag in tags:
                self.add_tag(tag)
        else:
            self.tags = self.tags.union(tags)
        return self

    def put_metadata(self, key, value):
        self.metadata[key] = value

    def get_path(self):
        return self.metadata["path"]

    def __str__(self):
        return "[{title}]({uri})".format(title=self.name, uri=self.uri)

    def __repr__(self):
        return "[{title}]({uri})".format(title=self.name, uri=self.uri)

    def __lt__(self, other):
        if self.get_path() != other.get_path():
            return self.get_path() < other.get_path()
        else:
            return self.name < other.name

    def __gt__(self, other):
        if self.get_path() != other.get_path():
            return self.get_path() > other.get_path()
        else:
            return self.name > other.name

    def __eq__(self, other):
        if self.get_path() != other.get_path():
            return False
        else:
            return self.name == other.name


class FileCollection:

    def __init__(self):
        # use list instead of complex map to support more flexible operation related to tags.
        self.collection = []

    def add_file(self, file: File):
        self.collection.append(file)
        return self

    def add_files(self, files: list):
        self.collection.extend(files)
        return self

    def add_collection(self, other):
        self.collection.extend(other.collection)

    def filter_by_path(self, root_path: Path):
        result = FileCollection()
        for file in self.collection:
            if file.get_path().is_child_of(root_path):
                result.add_file(file)
        return result

    def filter_by_path_(self, root_path: str):
        result = FileCollection()
        for file in self.collection:
            if file.get_path().is_child_of(Path(root_path)):
                result.add_file(file)
        return result

    def filter_by_tag(self, tag: str):
        result = FileCollection()
        for file in self.collection:
            if tag in file.tags:
                result.add_file(file)
        return result

    def filter_by_tags(self, tags: set):
        result = FileCollection()
        for file in self.collection:
            if len(file.tags.intersection(tags)) > 0:
                result.add_file(file)
        return result

    def __str__(self):
        return str(self.collection)

    def __repr__(self):
        return repr(self.collection)


if __name__ == '__main__':
    file1 = File("A", "a.c.d", Path("a/c/d"))
    file1.add_tag("good").add_tag("middle")
    file2 = File("B", "a.b.e", Path("a/b/e"))
    file2.add_tags({"bad", "middle"})

    file_col = FileCollection()
    file_col.add_files([file1, file2])

    print(file_col)
    print(file_col.filter_by_path(Path("a/c")))
    print(file_col.filter_by_tags({"bad"}))
    print(file_col.filter_by_tag("middle").filter_by_path(Path("a")))
