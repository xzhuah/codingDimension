# Created by Xinyu Zhu on 2022/7/22, 19:04
import pygetwindow
import pyautogui
import subprocess
from os import listdir
from os.path import isfile, join

functional_key = ['enter', 'f1', 'f2', 'f3', "left"]


# Key actions
def enter_key(string: str):
    pyautogui.write(string)


def enter_functional_key(functional_name: str):
    pyautogui.press(functional_name)


def enter_hot_key(functional_names: list):
    pyautogui.press(functional_names)


def click_mouse(x, y):
    pyautogui.click(x, y)


# Window actions
def get_window_by_title():
    pass


# System commands
def run_cmd(cmd: str):
    process = subprocess.Popen([cmd])


# filesystem info
def get_all_filenames(dir: str):
    onlyfiles = [f for f in listdir(dir) if isfile(join(dir, f))]
    return onlyfiles