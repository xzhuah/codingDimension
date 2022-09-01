# Created by Xinyu Zhu on 2022/7/12, 20:30

import pygetwindow
from threading import Thread
import os
import pyautogui
from os import listdir
from os.path import isfile, join
import time
import subprocess

# blender 安装路径，不能用空格，中文，和其他特殊字符，以下所有路径均需满足此项要求
blender_path = "C:/Software/blender3/Windows/Release/blender.exe"
blender_launcher = "C:/Software/blender3/Windows/Release/blender-launcher.exe"

# 如果一帧图片渲染超过这个时间(秒), 重启Blender, 防止因为意外情况卡死
MAX_WAIT_TIME_PER_FRAME = 60 * 20  # 默认20分钟，可根据实际情况设置


def last_image_done(folder_name, start_frame=0):
    onlyfiles = [f for f in listdir(folder_name) if isfile(join(folder_name, f))]
    max_frame = start_frame
    # all_frame = set()
    # all_frame.add(start_frame)
    for filename in onlyfiles:
        try:
            frame_num = int(filename.split(".")[-2][-4:])
            # all_frame.add(frame_num)
            if max_frame < frame_num:
                max_frame = frame_num
        except:
            # in case we have other file with mal format
            continue
    # while max_frame > start_frame and max_frame - 1 not in all_frame:
    #     max_frame -= 1
    return max_frame


def is_blender_running():
    for t in pygetwindow.getAllTitles():
        if t.startswith("Blender"):
            return True
    return False


def render_started():
    for t in pygetwindow.getAllTitles():
        if t.startswith("Blender渲染"):
            return True
    return False


def close_all_blender_window():
    for t in pygetwindow.getAllTitles():
        if t.startswith("Blender"):
            blender_window = pygetwindow.getWindowsWithTitle(t)[0]
            blender_window.close()
            time.sleep(1)
            # 点击不保存
            pyautogui.click(1299, 744)


def find_first_blender_window():
    for t in pygetwindow.getAllTitles():
        if t.startswith("Blender"):
            return pygetwindow.getWindowsWithTitle(t)[0]
    return False


# 使用命令行的方式来打开blender渲染
def render_in_background(start_frame: int, end_frame: int, target_project: str, blender_cmd=blender_path):
    # frames = list(range(start_frame, end_frame + 1))
    render_frame_range(start_frame, end_frame, target_project, blender_cmd)
    # render_frames(frames, target_project, blender_cmd)


# 使用按键精灵的方式来打开blender渲染
def render_in_foreground(start_frame: int, end_frame: int, target_project: str, skip_first_init=False):
    if not skip_first_init:
        # 以命令行启动blender并打开项目
        subprocess.Popen([blender_launcher, target_project], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        # 等待20秒程序启动
        time.sleep(15)

        window = find_first_blender_window()
        window.activate()
        window.maximize()
        # 等待2秒，让窗口稳定
        time.sleep(2)

        # 进入layout标签页
        pyautogui.click(332, 35)
        time.sleep(1)
        pyautogui.move(1, 1)
        time.sleep(1)
        pyautogui.click(331, 35)
        time.sleep(1)

        # 设定开始和结束帧

        time.sleep(1)
        pyautogui.click(1981, 1293)
        time.sleep(1)
        pyautogui.click(1981, 1292)
        print("setting start frame:", start_frame)
        time.sleep(0.5)
        pyautogui.press("backspace")
        for s in str(start_frame):
            time.sleep(0.5)
            pyautogui.press(s)
        # pyautogui.write(str(start_frame), interval=1)
        time.sleep(1)
        pyautogui.press('enter')
        time.sleep(1)

        time.sleep(1)
        pyautogui.click(2077, 1294)
        time.sleep(1)
        pyautogui.click(2076, 1294)
        print("setting end frame:", end_frame)
        time.sleep(0.5)
        pyautogui.press("backspace")
        for s in str(end_frame):
            time.sleep(0.5)
            pyautogui.press(s)

        # pyautogui.write(str(end_frame), interval=1)
        time.sleep(1)
        pyautogui.press('enter')
    time.sleep(1)
    while render_started() is False:
        press_render()


def press_render():
    # 开始渲染
    pyautogui.moveTo(121, 35)
    time.sleep(1)
    pyautogui.mouseDown()
    time.sleep(1)
    # pyautogui.click(119, 32)
    # time.sleep(1)
    # # pyautogui.move(0,1)
    # time.sleep(1)
    pyautogui.moveTo(121, 79, duration=0.5)
    time.sleep(1)
    pyautogui.mouseUp()
    time.sleep(1)
    # pyautogui.click(121, 80)


# 不要输入一个很长的数组，太长会报错
def render_frames(frames: list, target_project: str, blender_cmd=blender_path):
    frame_str = ",".join(map(str, frames))
    command_line = blender_cmd + " " + target_project + " -f " + frame_str
    print(command_line)
    os.system(command_line)


# 不要输入一个很长的数组，太长会报错
def render_frames(frames: list, target_project: str, save_path: str, blender_cmd=blender_path):
    frame_str = ",".join(map(str, frames))
    command_line = blender_cmd + " " + target_project + " -o" + save_path + " -f " + frame_str
    print(command_line)
    os.system(command_line)


def render_frame_range(start: int, end: int, target_project: str, blender_cmd=blender_path):
    # frame_str = ",".join(map(str, frames))
    command_line = blender_cmd + " " + target_project + " -f " + str(start) + ".." + str(end)
    print(command_line)
    os.system(command_line)


def start_render_remaining_file(task, skip_first_init=False):
    start_frame = last_image_done(task["target_folder"], task["start_frame"]) + 1
    end_frame = task["end_frame"]
    if end_frame >= start_frame:
        # render_in_background(start_frame, end_frame, task["project_file"])
        render_in_foreground(start_frame, end_frame, task["project_file"], skip_first_init)


def is_task_terminated_early(task):
    last_image_frame = last_image_done(task["target_folder"], task["start_frame"])
    return last_image_frame < task["end_frame"] and not is_blender_running(), last_image_frame >= task[
        "end_frame"], last_image_frame


# def force_render(task, check_time=10 * 6 * 5, small_check_time=60, skip_first_init=False):
#     global MAX_WAIT_TIME_PER_FRAME
#     waiting_time = 0
#     last_frame = 0
#     big_check_freq = 10
#     index = 0
#     while True:
#         index += 1
#         if is_blender_running():
#             if index % big_check_freq != 0:
#                 time.sleep(small_check_time)
#                 continue
#         killed, finish, last_image_frame = is_task_terminated_early(task)
#         # don't wait for more than 5 min, if more than 5 min, terminate and restart
#         # if waiting_time * check_time > MAX_WAIT_TIME_PER_FRAME:
#         #     close_all_blender_window()
#         #     time.sleep(check_time)
#         if finish:
#             close_all_blender_window()
#             break
#         if not killed:
#             if last_image_frame == last_frame:
#                 waiting_time += 1
#             else:
#                 last_frame = last_image_frame
#             time.sleep(check_time)
#         else:
#             Thread(target=start_render_remaining_file, args=(task, skip_first_init == True)).start()
#             waiting_time = 0
#             last_frame = 0
#             time.sleep(check_time)
#             skip_first_init = False
#

# currently support one task only
tasks = [
    {
        # blender 工程文件
        "project_file": "C:/myC/Personal/3DWorkSpace/Project/youla/keting.blend",
        # 图片输出路径
        "target_folder": "C:/myC/Personal/3DWorkSpace/Project/youla/image",
        # 起始帧
        "start_frame": 30,
        # 结束帧
        "end_frame": 5142
    }
]

if __name__ == '__main__':
    # print(last_image_done(tasks[0]["target_folder"]))

    # close_all_blender_window()
    # time.sleep(10)
    # # 自动托管渲染

    # 延时1小时开始
    # time.sleep(60*60*1)
    # force_render(tasks[0])

    # press_render()

    指定帧渲染
    render_frames([4395],
                  tasks[0]["project_file"])
