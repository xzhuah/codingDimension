# Created by Xinyu Zhu on 1/22/2023, 2:23 PM
# 为了更好地管理文件，我们设计了一套文件标签方案便于我们快速标记文件，并且根据这套标签对文件进行更好的分类索引
import pygetwindow
import pyautogui
import time
from node.systemAsistantNode.systemContext import get_system_context

import ctypes
from ctypes import wintypes
import psutil
import os
import pyperclip as pc
from common.io.file.PlainTextClient import read_file_from, wrtie_file
from common.time.TimeClient import quick_now
import json

#
TAGGER_FILE_TEMPLATE = "{target}_tagger.json"

# always copy to use
tagger_root = {"tagger_version": "1.0.0"}


def get_default_tagger_file_path(path):
    return os.path.join(path, TAGGER_FILE_TEMPLATE.format(target=""))


def get_base_tags(path: str):
    result = tagger_root.copy()
    result["path"] = path
    result["time"] = quick_now()
    return result


def read_tag_from_active_folder():
    context = get_system_context()
    if context["foreground_app"] != "explorer.exe":
        return None
    path = os.path.normpath(context["foreground_window"].title)
    if not os.path.exists(path):
        path = pc.paste()
        if not os.path.exists(path):
            return None
    target_file = os.path.join(path, TAGGER_FILE_TEMPLATE.format(target=""))
    if not os.path.exists(target_file):
        return get_base_tags(path)
    return json.loads(read_file_from(target_file))


def set_tag_to_active_folder(tag: dict, reset=False):
    existing_tag = read_tag_from_active_folder()
    if existing_tag is None:
        return None
    if reset:
        existing_tag = get_base_tags(os.path.normpath(get_system_context()["foreground_window"].title))
    for key in tag:
        existing_tag[key] = tag[key]
    wrtie_file(get_default_tagger_file_path(existing_tag["path"]), json.dumps(existing_tag))
    return existing_tag


def read_tag_from_only_folder():
    all_titles = pyautogui.getAllTitles()
    count = 0
    path = None
    for title in all_titles:
        if os.path.exists(title):
            count += 1
            path = title
    if count == 1:
        print(path)
    else:
        path = pc.paste()
        if not os.path.exists(path):
            print("Expect only one explore:", count, path)
            return None

    target_file = os.path.join(path, TAGGER_FILE_TEMPLATE.format(target=""))
    if not os.path.exists(target_file):
        return get_base_tags(path)
    return json.loads(read_file_from(target_file))


def set_tag_to_only_folder(tag: dict, reset=False):
    existing_tag = read_tag_from_only_folder()

    if existing_tag is None:
        return None
    if reset:

        all_titles = pyautogui.getAllTitles()
        count = 0
        path = None
        for title in all_titles:
            if os.path.exists(title):
                count += 1
                path = title
        if count == 1:
            print(path)
        else:
            path = pc.paste()
            if not os.path.exists(path):
                print("Expect only one explore:", count, path)
                return None

        existing_tag = get_base_tags(path)
    for key in tag:
        existing_tag[key] = tag[key]

    wrtie_file(get_default_tagger_file_path(existing_tag["path"]), json.dumps(existing_tag))
    return existing_tag


def close_all_file_browser():
    all_title_to_close = set()
    for title in pygetwindow.getAllTitles():
        if os.path.exists(title):
            all_title_to_close.add(title)
    for title in all_title_to_close:
        for w in pygetwindow.getWindowsWithTitle(title):
            if os.path.exists(w.title):
                w.close()


def open_file_browser_at(path):
    os.startfile(path)


import os
from pynput.keyboard import Key, Listener
from common.annotations.annotation import simple_thread


def on_press(key):
    if str(key).lower() == "'q'":
        print(set_tag_to_only_folder({
            "r18": 1  # 可用级别
        }))
    if str(key).lower() == "'w'":
        print(set_tag_to_only_folder({
            "r18": 2  # 无表情 可看级别
        }))
    if str(key).lower() == "'e'":
        print(set_tag_to_only_folder({
            "r18": 3  # 不可看级别
        }))
    if str(key).lower() == "'f'":
        pc_content = pc.paste()
        if os.path.exists(pc_content):
            close_all_file_browser()
            open_file_browser_at(pc_content)

        # for title in pygetwindow.getAllTitles():
        #     if os.path.exists(title):
        #         window = pygetwindow.getWindowsWithTitle(title)[0]
        #         window.activate()
        #         window.maximize()


@simple_thread
def start_listen():
    with Listener(on_press=on_press) as listener:
        listener.join()


if __name__ == '__main__':
    # print(os.path.exists)
    # while True:
    #     # window = pygetwindow.getActiveWindow()
    #     # print(window)
    #     # print(window.title)
    #
    #     time.sleep(1)
    #     print(read_tag_from_only_folder())
    start_listen()
