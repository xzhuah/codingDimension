# Created by Xinyu Zhu on 1/18/2023, 7:03 PM
import pyperclip as pc
import os
from pynput.keyboard import Key, Listener
from common.annotations.annotation import simple_thread

def is_file_path(text):
    if "\\" not in text:
        return False
    return os.path.exists(text)

def is_path_copied():
    return is_file_path(pc.paste())

def to_normal_path(win_path: str) -> str:
    return win_path.replace("\\", "/")

def on_press(key):
    if key == Key.ctrl or key == Key.ctrl_l or key == Key.ctrl_r:
        if is_path_copied():
            pc.copy(to_normal_path(pc.paste()))

@simple_thread
def start_listen():
    with Listener(on_press=on_press) as listener:
        listener.join()

