# Created by Xinyu Zhu on 1/2/2023, 12:39 AM

class BaseFileProcessor:
    def __init__(self):
        self.batch_size = 10

    def process_file(self, filepath):
        pass

    def process_filepath(self, filepath):
        return filepath
