# Created by Xinyu Zhu on 2021/8/28, 23:55
from node.tagTreeNote.models.Path import Path
from common.tools.utils import check_state
from node.tagTreeNote.utils.utils import verify_filename, verify_tag, verify_uri


class File:

    def __init__(self, filename: str, uri: str, path: Path):
        # name of file
        self.name = filename.strip()
        check_state(verify_filename(filename))
        # a string act as uri
        self.uri = uri.strip()
        check_state(verify_uri(self.uri))
        # a set for holding customer specified tags (str)
        self.tags = set()
        # metadata for the file, free_formed, open for extension
        # it must be a str to str map
        self.metadata = dict()
        # path of the file
        self.metadata["path"] = path.path
        self.path = path.copy()

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

    def put_metadata(self, key: str, value: str):
        self.metadata[key] = value
        return self

    def get_path(self) -> Path:
        return self.path

    def __str__(self):
        return "[{title}]({uri})".format(title=self.name, uri=self.uri)

    def __repr__(self):
        return "[{title}]({uri})".format(title=self.name, uri=self.uri)

    def __lt__(self, other):
        if self.get_path() != other.get_path():
            return self.get_path() < other.get_path()
        else:
            if self.name != other.name:
                return self.name < other.name
            else:
                return self.uri < other.uri

    def __gt__(self, other):
        if self.get_path() != other.get_path():
            return self.get_path() > other.get_path()
        else:
            if self.name != other.name:
                return self.name > other.name
            else:
                return self.uri > other.uri

    def __eq__(self, other):
        if self.get_path() != other.get_path():
            return False
        else:
            return self.name == other.name and self.uri == other.uri


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

    # relation between tags is or
    def filter_by_tags_or(self, tags: set):
        result = FileCollection()
        for file in self.collection:
            if len(file.tags.intersection(tags)) > 0:
                result.add_file(file)
        return result

    # relation between tags is and
    def filter_by_tags_and(self, tags: set):
        result = FileCollection()
        for file in self.collection:
            if len(file.tags.intersection(tags)) == len(tags):
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
    print(file_col.filter_by_tags_or({"bad"}))
    print(file_col.filter_by_tag("middle").filter_by_path(Path("a")))
