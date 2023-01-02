# Created by Xinyu Zhu on 2022/1/30, 19:08
import os
from os import listdir
from os.path import isfile, isdir, join
import hashlib
import threading
import shutil
import re

google_download_root = "C:/GoogleDownload/"


def get_all_filename(folder_path: str) -> list:
    return [f for f in listdir(folder_path) if isfile(join(folder_path, f))]

def get_all_filepath(folder_path: str) -> list:
    if not folder_path.endswith("/"):
        folder_path = folder_path + "/"
    return [folder_path + f for f in listdir(folder_path) if isfile(join(folder_path, f))]


def get_all_subfolder(folder_path: str) -> list:
    return [f for f in listdir(folder_path) if isdir(join(folder_path, f))]


# 批量去掉文件夹下的文件名称的后缀部分，不改变文件本来的类型，如a_b_v.zip 会变为a_b.zip
def batch_remove_filename_postfix(folder_path: str):
    all_filename = get_all_filename(folder_path)
    if not folder_path.endswith("/"):
        folder_path += "/"
    for filename in all_filename:
        oldname = folder_path + filename
        newname = folder_path + filename[0:filename.rindex("_")] + filename[filename.rindex("."):]
        try:
            os.rename(oldname, newname)
        except:
            pass


def rename_file_under_folder(folder_path: str, filename_proto):
    all_filename = get_all_filename(folder_path)
    if not folder_path.endswith("/"):
        folder_path += "/"
    for filename in all_filename:
        oldname = folder_path + filename
        newname = folder_path + filename_proto(filename)
        print(oldname, newname, filename)
        try:
            os.rename(oldname, newname)
        except:
            pass

def ensure_postfix(folder_path: str):
    abs_folder_path = folder_path
    if not abs_folder_path.endswith("/"):
        abs_folder_path += "/"
    return abs_folder_path


def is_filename_postfix_in(filename: str, target_set: set):
    if target_set is None or len(target_set) == 0:
        return True
    for postfix in target_set:
        if filename.lower().endswith(postfix.lower()):
            return True
    return False

def to_parent_folder(path: str):
    if "/" in path:
        return path[0: path.rindex("/") + 1]
    else:
        return "./"


def to_filename(path: str):
    if "/" in path:
        return path[path.rindex("/") + 1: ]
    else:
        return path


# 搜索文件夹下所有文件
def search_files(folder_path: str, post_filter: set):
    def __depth_first_search_files_helper__(current_folder: str, pre_result: list):
        for filename in get_all_filename(current_folder):
            if is_filename_postfix_in(filename, post_filter):
                pre_result.append(ensure_postfix(current_folder) + filename)
        all_folders = get_all_subfolder(current_folder)
        for folder in all_folders:
            __depth_first_search_files_helper__(ensure_postfix(current_folder) + folder, pre_result)

    all_file = []
    __depth_first_search_files_helper__(folder_path, all_file)
    return all_file


def material_rename_proto(old_name: str):
    new_name = old_name.replace("_N","_normal").replace("_G", "_roughness").replace("_M", "_metalness")

    return new_name
    # print(old_name)

def get_md5(filename):
    return hashlib.md5(open(filename,'rb').read()).hexdigest()

def calc_files(filenames, process, callback_dict):
    for file in filenames:
        callback_dict[file] = process(file)


# windows 路径去除反斜杠
def ensure_path_format(filepath: str):
    while "\\" in filepath:
        filepath = filepath.replace("\\", "/")
    return filepath


def get_files_md5(root, target_types, batch_size=10):
    all_files = search_files(root, target_types)

    batch_num = len(all_files) // batch_size
    result = []
    for batch in range(batch_num):
        result.append(all_files[batch * batch_size: (batch + 1) * batch_size])

    if len(all_files) % batch_size != 0:
        result.append(all_files[batch_num * batch_size:])

    all_md5 = {}
    all_tasks = [threading.Thread(target=calc_files, args=(filebatch, get_md5,  all_md5)) for filebatch in
                 result]

    [task.start() for task in all_tasks]
    [task.join() for task in all_tasks]
    return all_md5

def found_duplication():
    root = ensure_path_format("D:/Work/3DWorkspace/MMDResource/human")

    all_md5s = get_files_md5(root, {".pmx", ".pmd"})

    found = {}
    for filename in all_md5s:
        if all_md5s[filename] not in found:
            found[all_md5s[filename]] = filename
        else:
            if not ("异次元学者" in filename or "异次元学者" in found[all_md5s[filename]]):
                print("found potential duplication:")
                print(filename, found[all_md5s[filename]])


def move_directory(source, target):
    shutil.move(source, target)


def fetch_author_name_from_filename(filename: str):
    if "by_" in filename:
        return filename[filename.rindex("by_")+3:]
    return ""

def manage_folder_by_author(target_folder="C:/Software/MarvelousDesigner10/Models/"):
    all_folders = get_all_subfolder(target_folder)
    existing_author_folder = set()
    folder_to_author = {}
    for filename in all_folders:
        fetched_name = fetch_author_name_from_filename(filename)
        if fetched_name == "":
            existing_author_folder.add(filename)
        else:
            folder_to_author[filename] = fetched_name

    for folder in folder_to_author:
        if folder_to_author[folder] in existing_author_folder:
            print("Moving folder to author folder", folder)
            move_directory(target_folder + folder, target_folder + folder_to_author[folder] + "/" + folder)
        else:
            pass
            author_folder = target_folder + folder_to_author[folder]
            print("making new dir:", author_folder)
            os.mkdir(author_folder)
            new_folder = author_folder + "/" + folder
            move_directory(target_folder + folder, new_folder)
            existing_author_folder.add(folder_to_author[folder])
            print("Moving folder to author folder", target_folder + folder, new_folder)

def isMdFolder(folder):
    all_md_file = search_files(folder, {".zprj", ".zpac"})
    return len(all_md_file) > 0

def isBlenderFolder(folder):
    all_md_file = search_files(folder, {".blend"})
    return len(all_md_file) > 0

def classify_folder(folder_path):
    all_subfolder = get_all_subfolder(folder_path)
    if not folder_path.endswith("/"):
        folder_path += "/"
    for subFolder in all_subfolder:
        subFolder = folder_path + subFolder
        if isMdFolder(subFolder):
            print(subFolder, "MD")
            move_directory(subFolder, "D:/Work/3DWorkspace/Models/")
        elif isBlenderFolder(subFolder):
            print(subFolder, "Blend")
            move_directory(subFolder, "D:/Work/3DWorkspace/Stages/")
        else:
            print(subFolder, "unknown")




if __name__ == '__main__':
    # all_file = get_all_filename("C:\GoogleDownload")
    # print(all_file)

    batch_remove_filename_postfix(google_download_root)
    # #
    classify_folder(google_download_root)
    #
    manage_folder_by_author(target_folder="D:/Work/3DWorkspace/Models/")
    manage_folder_by_author(target_folder="D:/Work/3DWorkspace/Stages/")

    manage_folder_by_author(target_folder="D:/Work/3DWorkspace/MMDResource/stage/")

    found_duplication()




    # rename_file_under_folder("C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Model/external/stage/《原神》珊瑚宫 贴图修正版_by_Viero月城/《原神》珊瑚宫 贴图修正版/Tex/", material_rename_proto)

