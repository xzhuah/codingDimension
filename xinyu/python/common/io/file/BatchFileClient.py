# Created by Xinyu Zhu on 1/1/2023, 11:59 PM

# 批量文件处理服务
from pprint import pprint
import hashlib
import threading
import os
from collections import Counter
import time
import re
import shutil
from PIL import Image


def get_all_filepath(folder_path: str) -> list:
    folder_path = os.path.normpath(folder_path)
    return [os.path.join(folder_path, filename) for filename in os.listdir(folder_path) if
            os.path.isfile(os.path.join(folder_path, filename))]


def get_all_folderpath(folder_path: str) -> list:
    folder_path = os.path.normpath(folder_path)
    return [os.path.join(folder_path, filename) for filename in os.listdir(folder_path) if
            os.path.isdir(os.path.join(folder_path, filename))]


def get_all_subpath(folder_path: str) -> list:
    folder_path = os.path.normpath(folder_path)
    return [os.path.join(folder_path, filename) for filename in os.listdir(folder_path)]


class BaseFileProcessor:
    # Default implementation of file processor, search first, and then do the processing
    # thread safe.
    # batch_num: use at most batch_num workers to process files in parallel
    # max_depth: depth of searching files, 0 means only files under the root folder, 1 means files under the root folder
    # and files under the direct sub-folder of the root folder. -1 means no limit
    # target_depth: if >=0 then only process file at the target depth. target_depth need to be smaller or equal to
    # max_depth in order to be accessible.
    # mode: 0: file only, 1: folder only, 2: file and folder
    def __init__(self, batch_num=10, max_depth=-1, mode=0, target_depth=-1):
        self.batch_num = batch_num
        self.max_depth = max_depth
        self.target_depth = target_depth
        self.mode = mode
        self.global_stop = False

    # child class defined logic
    def filepath_filter(self, filepath: str) -> bool:
        return True

    def __depth_first_search_files_helper__(self, current_folder: str, pre_result: list, current_depth=0, max_depth=-1,
                                            target_depth=-1):
        if max_depth >= 0 and current_depth > max_depth:
            return
        if target_depth >= 0 and current_depth > target_depth:
            return
        if target_depth < 0 or target_depth == current_depth:
            if self.mode == 0:
                for filepath in get_all_filepath(current_folder):
                    if self.filepath_filter(filepath):
                        pre_result.append(filepath)
            elif self.mode == 1:
                for filepath in get_all_folderpath(current_folder):
                    if self.filepath_filter(filepath):
                        pre_result.append(filepath)
            elif self.mode == 2:
                for filepath in get_all_subpath(current_folder):
                    if self.filepath_filter(filepath):
                        pre_result.append(filepath)
            else:
                raise NotImplementedError("file search mode:{mode} is not implemented".format(mode=self.mode))

        all_folders = get_all_folderpath(current_folder)
        for folder in all_folders:
            self.__depth_first_search_files_helper__(folder, pre_result, current_depth + 1, max_depth, target_depth)

    def search_file(self, root: str) -> list:
        folder_path = os.path.normpath(root)
        all_file = []
        max_depth = self.max_depth
        target_depth = self.target_depth
        self.__depth_first_search_files_helper__(folder_path, all_file, 0, max_depth, target_depth)
        return all_file

    def process_with_combiner(self, root: str, combiner, initial_value):
        all_files = self.search_file(root)
        batch_num = self.batch_num if len(all_files) > self.batch_num else len(all_files)
        result = initial_value
        if batch_num > 1:
            all_workers = [threading.Thread(target=combiner, args=(all_files, result)) for _ in
                           range(batch_num)]
            [task.start() for task in all_workers]
            [task.join() for task in all_workers]
        else:
            # 支持单线程模式，兼容不需要并行的处理场景
            combiner(all_files, result)
        return result

    # 注意: 可以利用global_stop停掉所有线程, 也可以只停掉单个线程
    def process_file_with_early_stop_signal(self, filepath: str):
        return self.process_file(filepath), self.global_stop

    # child class defined logic
    def process_file(self, filepath: str):
        # suggest handle exception
        return filepath

    def process_filepath(self, filepath: str):
        return filepath

    def process_to_map(self, root: str) -> dict:
        def _combiner(filepaths: list, result_store: dict):
            while len(filepaths) > 0:
                # for thread-safe consideration
                try:
                    filepath = filepaths.pop()
                except Exception as e:
                    return
                file_result, early_stop = self.process_file_with_early_stop_signal(filepath)
                result_store[self.process_filepath(filepath)] = file_result
                if early_stop:
                    break
            return

        return self.process_with_combiner(root, _combiner, dict())

    def process_to_set(self, root: str) -> set:
        def _combiner(filepaths: list, result_store: set):
            while len(filepaths) > 0:
                # for thread-safe consideration
                try:
                    filepath = filepaths.pop()
                except Exception as e:
                    return
                file_result, early_stop = self.process_file_with_early_stop_signal(filepath)
                result_store.add(file_result)
                if early_stop:
                    break
            return

        return self.process_with_combiner(root, _combiner, set())

    def process_to_list(self, root: str) -> list:
        def _combiner(filepaths: list, result_store: list):
            while len(filepaths) > 0:
                # for thread-safe consideration
                try:
                    filepath = filepaths.pop()
                except Exception as e:
                    return
                file_result, early_stop = self.process_file_with_early_stop_signal(filepath)
                result_store.append(file_result)
                if early_stop:
                    break
            return

        return self.process_with_combiner(root, _combiner, [])

    def process(self, root: str):
        def _combiner(filepaths: list, initial_value=0):
            while len(filepaths) > 0:
                # for thread-safe consideration
                try:
                    filepath = filepaths.pop()
                except Exception as e:
                    return
                file_result, early_stop = self.process_file_with_early_stop_signal(filepath)
                if early_stop:
                    break
            return

        return self.process_with_combiner(root, _combiner, None)


# for testing
class MyBatchFileProcessor(BaseFileProcessor):
    def filepath_filter(self, filepath: str) -> bool:
        return True

    def process_file(self, filepath: str):
        return filepath


class Md5Processor(BaseFileProcessor):
    def filepath_filter(self, filepath: str) -> bool:
        return filepath.endswith(".pmx")

    def process_file(self, filepath: str):
        return hashlib.md5(open(filepath, 'rb').read()).hexdigest()


# 获取文件后缀, 自动转小写，无须文件读取 batch_num should be 1 or 2
class FileTypeProcessor(BaseFileProcessor):
    def __init__(self, batch_num=1, max_depth=-1, to_lower=True):
        super(FileTypeProcessor, self).__init__(batch_num=batch_num, max_depth=max_depth)
        self.to_lower = to_lower

    def process_file(self, filepath: str) -> str:
        postfix = os.path.splitext(filepath)[1]
        if self.to_lower:
            return postfix.lower()
        else:
            return postfix


# 获取文件夹下所有文件的后缀统计, 自动转小写
class FolderFileTypeProcessor(BaseFileProcessor):
    def __init__(self, batch_num=2, to_lower=True):
        super(FolderFileTypeProcessor, self).__init__(batch_num=batch_num, max_depth=0, mode=1)
        self.fileTypeProcessor = FileTypeProcessor(batch_num=1, max_depth=-1, to_lower=to_lower)

    def process_file(self, filepath: str):
        all_type = self.fileTypeProcessor.process_to_list(filepath)
        return Counter(all_type)


class FolderClassificationProcessor(BaseFileProcessor):
    def __init__(self, batch_num=2):
        super(FolderClassificationProcessor, self).__init__(batch_num=batch_num, max_depth=0, target_depth=-1, mode=1)
        self.file_classifier = [".zprj", ".zpac", ".blend", ".pmx", ".pmd", ".vmd"]
        self.file_type_processor = FileTypeProcessor(batch_num=1, to_lower=True)

    def process_file(self, filepath: str):
        all_type = self.file_type_processor.process_to_set(filepath)
        for classifier in self.file_classifier:
            if classifier in all_type:
                return classifier
        return None


class AplayboxDownloadFileRenameProcessor(BaseFileProcessor):
    def __init__(self, batch_num=2):
        super(AplayboxDownloadFileRenameProcessor, self).__init__(batch_num=batch_num, max_depth=0, target_depth=0,
                                                                  mode=0)
        self.aplay_file_format = re.compile("[^_]+_by_[^_]+_[^_]+")

    def filepath_filter(self, filepath: str) -> bool:
        postfix = os.path.splitext(filepath)[1]
        if postfix not in {".zip", ".vmd", ".rar"}:
            return False

        filename = os.path.basename(filepath)
        return bool(self.aplay_file_format.search(filename))

    def process_file(self, filepath: str):
        folder = os.path.dirname(filepath)
        filename = os.path.basename(filepath)
        new_name = filename[0:filename.rindex("_")] + filename[filename.rindex("."):]
        new_path = os.path.join(folder, new_name)
        os.rename(filepath, new_path)
        return new_path


class AplayboxDownloadFolderClassifierProcessor(FolderClassificationProcessor):
    def __init__(self, batch_num=6):
        super(AplayboxDownloadFolderClassifierProcessor, self).__init__(batch_num=batch_num)
        self.aplay_folder_format = re.compile("[^_]+_by_[^_]+")
        self.md_file_root = "D:/Work/3DWorkspace/Models/"
        self.stage_file_root = "D:/Work/3DWorkspace/Stages/"
        self.file_type_to_root = {
            ".zprj": "D:/Work/3DWorkspace/Models/",
            ".zpac": "D:/Work/3DWorkspace/Models/",
            ".blend": "D:/Work/3DWorkspace/Stages/"
        }

    def filepath_filter(self, filepath: str) -> bool:
        filename = os.path.basename(filepath)
        return bool(self.aplay_folder_format.search(filename))

    def process_file(self, filepath: str):

        folder_name = os.path.basename(filepath)
        author_name = folder_name[folder_name.rindex("by_") + 3:]
        all_type = self.file_type_processor.process_to_set(filepath)
        target_path = None
        for classifier in self.file_classifier:
            if classifier in all_type:
                if classifier in self.file_type_to_root:
                    target_path = os.path.join(self.file_type_to_root[classifier], author_name)
                break

        if target_path is not None:
            if not os.path.exists(target_path):
                os.mkdir(target_path)
            target = os.path.normpath(os.path.join(target_path, folder_name))
            shutil.move(filepath, target)
            return target
        return None


class Image4KTo2KProcessor(BaseFileProcessor):
    def __init__(self, batch_num=32):
        super(Image4KTo2KProcessor, self).__init__(batch_num=batch_num, max_depth=0, mode=0, target_depth=-1)
        self.out_root = "C:/Users/Xinyu Zhu/Pictures/2k/"
        if not os.path.exists(self.out_root):
            os.mkdir(self.out_root)

    def filepath_filter(self, filepath: str) -> bool:
        return os.path.splitext(filepath)[1] in {".png", ".jpg"}

    def process_file(self, filepath: str):
        image_file = Image.open(filepath)
        if image_file.width > 2560 or image_file.height > 1440:
            image_file.resize((2560, 1440))
        reduced_image_path = os.path.normpath(os.path.join(self.out_root, os.path.basename(filepath)))
        image_file.save(reduced_image_path)
        return reduced_image_path


if __name__ == '__main__':
    # duration = 0
    # for i in range(100):
    #     now = time.perf_counter()
    #     processor = FileTypeProcessor()
    #     result = processor.process_to_map("D:/Work/3DWorkspace/Models")
    #     duration += time.perf_counter() - now
    # print(duration)

    # rename_processor = AplayboxDownloadFileRenameProcessor()
    # result = rename_processor.process_to_map("C:/GoogleDownload")
    #
    # folder_move_processor = AplayboxDownloadFolderClassifierProcessor()
    # result = folder_move_processor.process_to_map("C:/GoogleDownload")

    image4k_to_2k = Image4KTo2KProcessor()
    result = image4k_to_2k.process_to_map("C:/Users/Xinyu Zhu/Pictures/4K高清壁纸")

    pprint(result)

    print(len(result))
