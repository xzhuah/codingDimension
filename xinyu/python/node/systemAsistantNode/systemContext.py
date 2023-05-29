# Created by Xinyu Zhu on 1/22/2023, 2:34 PM
import pygetwindow
import pyautogui
import ctypes
from ctypes import wintypes
import psutil
import time

def get_system_context():
    system_context = {

    }

    user32 = ctypes.windll.user32
    h_wnd = user32.GetForegroundWindow()
    pid = wintypes.DWORD()
    user32.GetWindowThreadProcessId(h_wnd, ctypes.byref(pid))
    system_context["foreground_app"] = psutil.Process(pid.value).name()

    system_context["foreground_window"] =  pygetwindow.getActiveWindow()
    # window =
    # print(window)
    # print(window.title)


    return system_context

if __name__ == '__main__':
    time.sleep(2)
    context = get_system_context()
    print(context["foreground_window"].title)