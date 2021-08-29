# Created by Xinyu Zhu on 2021/8/28, 23:55
from node.tagTreeNote.Path import Path


class File:

    def __init__(self, filename: str, uri: str, path: Path):
        # name of file
        self.name = filename
        # a string act as uri
        self.uri = uri
        # path of the file
        self.path = path
        # a set for holding customer specified tags (str)
        self.tags = set()
        # metadata for the file, free_formed, open for extension
        self.metadata = dict()

    def add_tag(self, tag: str):
        self.tags.add(tag)

    def add_all_tags(self, tags):
        self.tags = self.tags.union(tags)

    def __str__(self):
        return "{title}, {uri}".format(title=self.name, uri=self.uri)

    def __repr__(self):
        return "{title}, {uri}".format(title=self.name, uri=self.uri)

    def __lt__(self, other):
        if self.path != other.path:
            return self.path < other.path
        else:
            return self.name < other.name

    def __gt__(self, other):
        if self.path != other.path:
            return self.path > other.path
        else:
            return self.name > other.name

    def __eq__(self, other):
        if self.path != other.path:
            return False
        else:
            return self.name == other.name


class FileCollection:

    def __init__(self):
        pass
