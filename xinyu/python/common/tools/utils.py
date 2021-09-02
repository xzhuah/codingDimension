# Created by Xinyu Zhu on 2021/8/29, 0:11
def check_state(expression, error_message=""):
    if not expression:
        raise Exception(error_message)


class ListSortHelper:
    def __init__(self, tags: list):
        # [tag1, tag2, ..., tags]
        check_state(len(tags) > 0)
        self.tags = tags

    def __str__(self):
        return str(self.tags)

    def __repr__(self):
        return str(self.tags)

    def __lt__(self, other):
        for i in range(min(len(self.tags), len(other.tags))):
            if self.tags[i] != other.tags[i]:
                return self.tags[i] < other.tags[i]
        # shorter is smaller
        return len(self.tags) < len(other.tags)

    def __gt__(self, other):
        for i in range(min(len(self.tags), len(other.tags))):
            if self.tags[i] != other.tags[i]:
                return self.tags[i] > other.tags[i]
        # longer is larger
        return len(self.tags) > len(other.tags)

    def __eq__(self, other):
        if len(self.tags) != len(other.tags):
            return False
        for i in range(len(self.tags)):
            if self.tags[i] != other.tags[i]:
                return False
        return True
