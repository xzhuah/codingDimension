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
from collections import defaultdict
from common.io.file.PlainTextClient import wrtie_file
from urllib.request import pathname2url
import zipfile
from pathlib import Path


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


class BaseJobProcessor:
    # batch_num: use at most batch_num workers to process files in parallel
    # batch_mode: 0: each thread work as busy as possible, no more than batch_num thread work at the same time
    # 1: each thread takes equal number of work, use batch_num to calculate size of work for each thread
    # 2: each thread takes equal number of work, use max_batch_size to calculate how many thread needed and then equally
    # distribute work to them.
    # 3: use max_batch_size as the batch size
    def __init__(self, batch_num=10, batch_mode=0,
                 max_batch_size=150):
        self.batch_num = batch_num
        self.batch_mode = batch_mode
        self.max_batch_size = max_batch_size
        self.global_stop = False

        # log some performance information
        self.logging_store = {}

    def process_with_combiner(self, jobs: list, combiner, initial_value):
        self.logging_store = {}
        result = initial_value

        if self.batch_mode == 0:
            batch_num = self.batch_num if len(jobs) > self.batch_num else len(jobs)
            if batch_num > 1:
                all_workers = [threading.Thread(target=combiner, args=(jobs, result)) for _ in
                               range(batch_num)]
                [task.start() for task in all_workers]
                [task.join() for task in all_workers]
            else:
                # 支持单线程模式，兼容不需要并行的处理场景
                combiner(jobs, result)
        elif self.batch_mode == 1:
            batch_num = self.batch_num if len(jobs) > self.batch_num else len(jobs)
            if batch_num > 1:
                batch_size = len(jobs) // batch_num
                if len(jobs) % batch_num != 0:
                    batch_size += 1

                batch_index = 0
                all_workers = []
                while (batch_index + 1) * batch_size <= len(jobs):
                    next_batch = jobs[batch_index * batch_size: (batch_index + 1) * batch_size]
                    all_workers.append(threading.Thread(target=combiner, args=(next_batch, result)))
                    batch_index += 1

                if len(jobs) % batch_size != 0:
                    next_batch = jobs[batch_index * batch_size:]
                    all_workers.append(threading.Thread(target=combiner, args=(next_batch, result)))

                [task.start() for task in all_workers]
                [task.join() for task in all_workers]
            else:
                # 支持单线程模式，兼容不需要并行的处理场景
                combiner(jobs, result)
        elif self.batch_mode == 2:
            if self.max_batch_size >= len(jobs):
                # 支持单线程模式，兼容不需要并行的处理场景
                combiner(jobs, result)
                return result
            else:
                batch_num = len(jobs) // self.max_batch_size
                if len(jobs) % self.max_batch_size != 0:
                    batch_num += 1

                batch_size = len(jobs) // batch_num
                if len(jobs) % batch_num != 0:
                    batch_size += 1

                batch_index = 0
                all_workers = []
                while (batch_index + 1) * batch_size <= len(jobs):
                    next_batch = jobs[batch_index * batch_size: (batch_index + 1) * batch_size]
                    all_workers.append(threading.Thread(target=combiner, args=(next_batch, result)))
                    batch_index += 1

                if len(jobs) % batch_size != 0:
                    next_batch = jobs[batch_index * batch_size:]
                    all_workers.append(threading.Thread(target=combiner, args=(next_batch, result)))

                [task.start() for task in all_workers]
                [task.join() for task in all_workers]
        elif self.batch_mode == 3:
            if self.max_batch_size >= len(jobs):
                # 支持单线程模式，兼容不需要并行的处理场景
                combiner(jobs, result)
                return result
            else:
                batch_size = self.max_batch_size
                batch_index = 0
                all_workers = []
                while (batch_index + 1) * batch_size <= len(jobs):
                    next_batch = jobs[batch_index * batch_size: (batch_index + 1) * batch_size]
                    all_workers.append(threading.Thread(target=combiner, args=(next_batch, result)))
                    batch_index += 1

                if len(jobs) % batch_size != 0:
                    next_batch = jobs[batch_index * batch_size:]
                    all_workers.append(threading.Thread(target=combiner, args=(next_batch, result)))

                [task.start() for task in all_workers]
                [task.join() for task in all_workers]

        return result

    # 注意: 可以利用global_stop停掉所有线程, 也可以只停掉单个线程
    def process_job_with_early_stop_signal(self, job):
        return self.process_job(job), self.global_stop

    # child class defined logic
    def process_job(self, job):
        # suggest handle exception
        return job

    def job_to_key(self, job):
        return job

    def process_to_map(self, jobs: list) -> dict:
        def _combiner(jobs: list, result_store: dict):
            work_load = 0
            start_time = time.perf_counter()
            while len(jobs) > 0:
                # for thread-safe consideration
                try:
                    job = jobs.pop()
                except Exception as e:
                    break
                result, early_stop = self.process_job_with_early_stop_signal(job)
                work_load += 1
                result_store[self.job_to_key(job)] = result
                if early_stop:
                    break
            self.combiner_logging(start_time, work_load)
            return

        return self.process_with_combiner(jobs, _combiner, dict())

    def process_to_set(self, jobs: list) -> set:
        def _combiner(jobs: list, result_store: set):
            work_load = 0
            start_time = time.perf_counter()
            while len(jobs) > 0:
                # for thread-safe consideration
                try:
                    job = jobs.pop()
                except Exception as e:
                    break
                result, early_stop = self.process_job_with_early_stop_signal(job)
                work_load += 1
                result_store.add(result)
                if early_stop:
                    break
            self.combiner_logging(start_time, work_load)
            return

        return self.process_with_combiner(jobs, _combiner, set())

    def process_to_list(self, jobs: list) -> list:
        def _combiner(jobs: list, result_store: list):
            work_load = 0
            start_time = time.perf_counter()
            while len(jobs) > 0:
                # for thread-safe consideration
                try:
                    job = jobs.pop()
                except Exception as e:
                    break
                result, early_stop = self.process_job_with_early_stop_signal(job)
                work_load += 1
                result_store.append(result)
                if early_stop:
                    break
            self.combiner_logging(start_time, work_load)
            return

        return self.process_with_combiner(jobs, _combiner, [])

    def process(self, jobs: list):
        def _combiner(jobs: list, initial_value=0):
            work_load = 0
            start_time = time.perf_counter()
            while len(jobs) > 0:
                # for thread-safe consideration
                try:
                    job = jobs.pop()
                except Exception as e:
                    break
                result, early_stop = self.process_job_with_early_stop_signal(job)
                work_load += 1
                if early_stop:
                    break
            self.combiner_logging(start_time, work_load)
            return

        return self.process_with_combiner(jobs, _combiner, None)

    def combiner_logging(self, start_time, work_load):
        end_time = time.perf_counter()
        thread_id = threading.get_ident()
        self.logging_store[thread_id] = {
            "thread_id": thread_id,
            "start_time": start_time,
            "end_time": end_time,
            "duration": end_time - start_time,
            "work_load": work_load
        }

    def profiling_summary(self):
        pprint(self.logging_store)


class BaseFileProcessor(BaseJobProcessor):
    # Default implementation of file processor, search first, and then do the processing
    # thread un-safe. BaseFileProcessor already handles parallel processing, don't use BaseFileProcessor instances in
    # into another thread pool that might change its attribute during execution.

    # max_depth: depth of searching files, 0 means only files under the root folder, 1 means files under the root folder
    # and files under the direct sub-folder of the root folder. -1 means no limit
    # target_depth: if >=0 then only process file at the target depth. target_depth need to be smaller or equal to
    # max_depth in order to be accessible.
    # file_select_mode: 0: file only, 1: folder only, 2: file and folder
    def __init__(self, batch_num=10, max_depth=-1, target_depth=-1, file_select_mode=0, batch_mode=0,
                 max_batch_size=150):
        super(BaseFileProcessor, self).__init__(batch_num=batch_num, batch_mode=batch_mode,
                                                max_batch_size=max_batch_size)
        self.max_depth = max_depth
        self.target_depth = target_depth
        self.file_select_mode = file_select_mode

        # allow child thread read the starting root
        self.root = "./"

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
            if self.file_select_mode == 0:
                for filepath in get_all_filepath(current_folder):
                    if self.filepath_filter(filepath):
                        pre_result.append(filepath)
            elif self.file_select_mode == 1:
                for filepath in get_all_folderpath(current_folder):
                    if self.filepath_filter(filepath):
                        pre_result.append(filepath)
            elif self.file_select_mode == 2:
                for filepath in get_all_subpath(current_folder):
                    if self.filepath_filter(filepath):
                        pre_result.append(filepath)
            else:
                raise NotImplementedError(
                    "file search mode:{mode} is not implemented".format(mode=self.file_select_mode))

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

    def process_job(self, job):
        return self.process_file(job)

    def job_to_key(self, job):
        return self.process_filepath(job)

    # child class defined logic
    def process_file(self, filepath: str):
        # suggest handle exception
        return filepath

    def process_filepath(self, filepath: str):
        return filepath

    def process_to_map(self, root: str) -> dict:
        self.root = root
        all_files = self.search_file(root)
        return super(BaseFileProcessor, self).process_to_map(all_files)

    def process_to_set(self, root: str) -> set:
        self.root = root
        all_files = self.search_file(root)
        return super(BaseFileProcessor, self).process_to_set(all_files)

    def process_to_list(self, root: str) -> list:
        self.root = root
        all_files = self.search_file(root)
        return super(BaseFileProcessor, self).process_to_list(all_files)

    def process(self, root: str):
        self.root = root
        all_files = self.search_file(root)
        return super(BaseFileProcessor, self).process(all_files)


class BatchFileWriter(BaseJobProcessor):
    def __init__(self, batch_num=10):
        super(BatchFileWriter, self).__init__(batch_num, 0, 150)

    def process_job(self, job):
        wrtie_file(job["path"], job["content"])
        return job["path"]

    def job_to_key(self, job):
        return job["path"]


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


# 重命名
class BatchFileRenameProcessor(BaseFileProcessor):
    def __init__(self, batch_num=2):
        super(BatchFileRenameProcessor, self).__init__(batch_num=batch_num, max_depth=0, target_depth=0,
                                                       file_select_mode=0)

    def filepath_filter(self, filepath: str) -> bool:
        postfix = os.path.splitext(filepath)[1]
        if postfix not in {".png"}:
            return False

        filename = os.path.basename(filepath)
        return filename.startswith("c2")

    def process_file(self, filepath: str):
        folder = os.path.dirname(filepath)
        filename = os.path.basename(filepath)
        new_name = filename.replace("c2", "")
        new_path = os.path.join(folder, new_name)
        os.rename(filepath, new_path)
        return new_path


# 获取文件夹下所有文件的后缀统计, 自动转小写
class FolderFileTypeProcessor(BaseFileProcessor):
    def __init__(self, batch_num=2, to_lower=True):
        super(FolderFileTypeProcessor, self).__init__(batch_num=batch_num, max_depth=0, file_select_mode=1)
        self.fileTypeProcessor = FileTypeProcessor(batch_num=1, max_depth=-1, to_lower=to_lower)

    def process_file(self, filepath: str):
        all_type = self.fileTypeProcessor.process_to_list(filepath)
        return Counter(all_type)


class FolderClassificationProcessor(BaseFileProcessor):
    def __init__(self, batch_num=2):
        super(FolderClassificationProcessor, self).__init__(batch_num=batch_num, max_depth=0, target_depth=-1,
                                                            file_select_mode=1)
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
                                                                  file_select_mode=0)
        self.aplay_file_format = re.compile("[^_]+[_]+by[_]+[^_]+[_]+[^_]+")

    def filepath_filter(self, filepath: str) -> bool:
        postfix = os.path.splitext(filepath)[1]
        if postfix not in {".zip", ".vmd", ".vpd", ".rar"}:
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
        super(Image4KTo2KProcessor, self).__init__(batch_num=batch_num, max_depth=0, file_select_mode=0,
                                                   target_depth=-1)
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


class YcyxzPreviewToWebpage(BaseFileProcessor):
    # "_ycyxzPreview.gif", "_ycyxzPreview.png"
    def __init__(self, postfix="_ycyxzPreview.png", source_postfix=".pmx", max_batch_size=150):
        super(YcyxzPreviewToWebpage, self).__init__(batch_num=-1, max_depth=-1, file_select_mode=0,
                                                    target_depth=-1, batch_mode=2, max_batch_size=max_batch_size)
        self.preview_postfix = postfix
        self.source_postfix = source_postfix

        self.webpages = defaultdict(str)
        self.batchFileWriter = BatchFileWriter()

    def filepath_filter(self, filepath: str) -> bool:
        return filepath.endswith(self.preview_postfix)

    def process_file(self, filepath: str):
        # source path might not exist
        source_path = os.path.join(os.path.dirname(filepath),
                                   os.path.basename(filepath).replace(self.preview_postfix, self.source_postfix))

        dir_path = os.path.dirname(filepath)

        item = {
            "image": os.path.relpath(filepath, self.root),
            "dir_path": os.path.relpath(dir_path, self.root),
            "info": self.source_file_to_info(source_path)
        }

        self.webpages[threading.get_ident()] += (self.info_to_html_template(item) + "\n")
        return

    def source_file_to_info(self, filepath):
        if self.source_postfix in {".pmx", ".pmd", ".zprj", ".zpac", ".blend"}:
            return {
                "model_path": os.path.relpath(filepath, self.root),
                "model_name": os.path.basename(filepath).replace(self.source_postfix, "")
            }
        elif self.source_postfix == ".vmd":
            return {
                "model_path": os.path.relpath(filepath, self.root),
                "model_name": os.path.basename(filepath).replace(self.source_postfix, "")
            }

        return

    def info_to_html_template(self, item) -> str:
        if self.source_postfix in {".pmx", ".pmd", ".zprj", ".zpac", ".blend"}:
            template = """
                     <div class="gallery">
                       <a target="_blank" href="{folder}">
                         <img src="{image_path}" alt="404", title="{title}">
                       </a>
                       <h2 class="desc">{model_name}</h2>
                     </div>
                     """
            return template.format(folder=item["dir_path"], image_path=pathname2url(item["image"]),
                                   title=item["info"]["model_name"],
                                   model_name=item["info"]["model_name"])
        elif self.source_postfix == ".vmd":
            pass

        return ""

    def generate_webpage(self, root: str, html_prefix="preview"):
        self.webpages.clear()
        super().process(root)
        template = """
        <html>
        <head>
        <style>
        div.gallery {
          margin: 5px;
          border: 1px solid #ccc;
          float: left;
          width: {image_width};
          height: {image_height};
        }

        div.gallery:hover {
          border: 1px solid #777;
        }

        div.gallery img {
          width: 100%;
          height: auto;
        }

        h2.desc {
          padding: 15px;
          text-align: center;
        }

        div.desc {
          padding: 15px;
          text-align: center;
        }
        </style>
        </head>
        <body>

        {content}

        </body>
        </html>

            """
        jobs = []
        if self.source_postfix in {".pmx", ".pmd"}:
            image_width = 600
            image_height = 550
            for i, key in enumerate(self.webpages):
                jobs.append({
                    "path": os.path.join(root, html_prefix + str(i) + ".html"),
                    "content": template.replace("{content}", self.webpages[key]).replace("{image_width}", str(
                        image_width) + "px").replace("{image_height}", str(image_height) + "px")
                })
        elif self.source_postfix in {".zprj", ".zpac"}:
            image_width = 900
            image_height = 577
            for i, key in enumerate(self.webpages):
                jobs.append({
                    "path": os.path.join(root, html_prefix + str(i) + ".html"),
                    "content": template.replace("{content}", self.webpages[key]).replace("{image_width}", str(
                        image_width) + "px").replace("{image_height}", str(image_height) + "px")
                })
        elif self.source_postfix in {".blend"}:
            image_width = 900
            image_height = 600
            for i, key in enumerate(self.webpages):
                jobs.append({
                    "path": os.path.join(root, html_prefix + str(i) + ".html"),
                    "content": template.replace("{content}", self.webpages[key]).replace("{image_width}", str(
                        image_width) + "px").replace("{image_height}", str(image_height) + "px")
                })
        elif self.source_postfix in {".vmd"}:
            image_width = 600
            image_height = 550
            for i, key in enumerate(self.webpages):
                jobs.append({
                    "path": os.path.join(root, html_prefix + str(i) + ".html"),
                    "content": template.replace("{content}", self.webpages[key]).replace("{image_width}", str(
                        image_width) + "px").replace("{image_height}", str(image_height) + "px")
                })
        self.batchFileWriter.process(jobs)

    def profiling_summary(self):
        super(YcyxzPreviewToWebpage, self).profiling_summary()
        self.batchFileWriter.profiling_summary()


# for testing
class MyBatchFileProcessor(BaseFileProcessor):
    def __init__(self):
        super(MyBatchFileProcessor, self).__init__(batch_num=50, batch_mode=2, max_batch_size=50)

    def filepath_filter(self, filepath: str) -> bool:
        return os.path.splitext(filepath)[1] in {".pmx"}

    def process_file(self, filepath: str):
        return filepath


if __name__ == '__main__':
    # f = FolderFileTypeProcessor()
    # print(f.process_to_map("D:/Work/3DWorkspace/MMDResource/human"))
    # f.profiling_summary()

    pass
    # duration = 0
    # for i in range(100):
    #     now = time.perf_counter()
    #     processor = FileTypeProcessor()
    #     result = processor.process_to_map("D:/Work/3DWorkspace/Models")
    #     duration += time.perf_counter() - now
    # print(duration)

    #
    # rename_processor = AplayboxDownloadFileRenameProcessor()
    # result = rename_processor.process_to_map("C:/GoogleDownload")
    # rename_processor.profiling_summary()
    #
    folder_move_processor = AplayboxDownloadFolderClassifierProcessor()
    result = folder_move_processor.process_to_map("C:/GoogleDownload")
    folder_move_processor.profiling_summary()

    # unzip_process = FileUnzipProcessor()
    # result = unzip_process.process_to_map("C:/GoogleDownload")
    # unzip_process.profiling_summary()
    # print(result)

    # processor = BatchFileRenameProcessor()
    # result = processor.process_to_map("C:/myC/Personal/3DWorkSpace/Project/R/leidian_s")
    # processor.profiling_summary()

    # my_test_processor = YcyxzPreviewToWebpage(postfix="_ycyxzPreview.png", source_postfix=".pmx", max_batch_size=150)
    # my_test_processor.generate_webpage("D:/Work/3DWorkspace/MMDResource/human/精选/机动战士牛肉式改/SiganlK_")
    # my_test_processor.profiling_summary()

    # image4k_to_2k = Image4KTo2KProcessor()
    # result = image4k_to_2k.process_to_map("C:/Users/Xinyu Zhu/Pictures/4K高清壁纸")

    # pprint(result)
    #
    # print(len(result))
