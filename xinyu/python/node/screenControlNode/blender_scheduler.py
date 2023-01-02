# Created by Xinyu Zhu on 2022/8/10, 23:23

import pygetwindow
from threading import Thread
import os
import pyautogui
from os import listdir
from os.path import isfile, join
import time
import subprocess
import re
from urllib import parse
#


def to_parent_folder(path: str):
    if "/" in path:
        return path[0: path.rindex("/") + 1]
    else:
        return "./"


def to_model_name(model_path: str):
    if "/" in model_path:
        return model_path[model_path.rindex("/") + 1:model_path.rindex(".")]
    else:
        return model_path[: int(model_path.rindex("."))]

def image_template2(image_path: str):
    template = """
<div class="gallery">
<a target="_blank" href="{image_path}">
    <img src="{image_path}" alt="404" title={image_path}>
     </a>
</div>
"""
    return template.format(folder=parse.quote(to_parent_folder(image_path)), image_path=parse.quote(image_path),
                           model_name=to_model_name(image_path))


def html_template2(content: str, image_width=600, image_height="auto"):
    template = """
<html>
<head>
<style>
div.gallery {
  margin: 0px;
  border: 0px solid #ccc;
  float: left;
  width: {image_width};
  height: {image_height};
}

div.gallery:hover {
  border: 0px solid #777;
}

div.gallery img {
  width: 100%;
  height: auto;
}

h2.desc {
  padding: 0px;
  text-align: center;
}

div.desc {
  padding: 0px;
  text-align: center;
}
</style>
</head>
<body>

{content}

</body>
</html>

    """
    if image_height != "auto":
        image_height = str(image_height) + "px"
    return template.replace("{content}", content).replace("{image_width}", str(image_width) + "px").replace(
        "{image_height}", image_height)


def to_relative_path(reference_abs_path: str, abs_path: str):
    return abs_path.replace(reference_abs_path, "")

def ensure_postfix(folder_path: str):
    abs_folder_path = folder_path
    if not abs_folder_path.endswith("/"):
        abs_folder_path += "/"
    return abs_folder_path


def preview_image_batch(root, image_paths, output_filename="preview.html"):
    image_components = []
    for pic_file in image_paths:
        image_components.append(image_template2(to_relative_path(root, pic_file)))
    image_content = "\n".join(image_components)
    index = 24
    html = html_template2(image_content, image_width=80 * index, image_height=160 * index)
    with open(ensure_postfix(root) + output_filename, mode="w", encoding="utf-8") as f:
        f.write(html)
    print(len(image_paths))


def preview_images_by_frames(root, frames, output_filename="preview.html"):
    image_paths = []
    for frame in frames:
        filename = str(frame)
        while len(filename) < 4:
            filename = "0" + filename
        filename += ".png"
        target_file = root + filename
        image_paths.append(target_file)
    preview_image_batch(root, image_paths, output_filename)

# blender 安装路径，不能用空格，中文，和其他特殊字符，以下所有路径均需满足此项要求
blender_path = "C:/Software/blender3/Windows/Release/blender.exe"
blender_launcher = "C:/Software/blender3/Windows/Release/blender-launcher.exe"


##################################################
# functions that help schedule the frames to be
# rendered
##################################################


# return a sorted list of rendered index
def list_all_rendered_image_index(folder_name: str) -> list:
    onlyfiles = [f for f in listdir(folder_name) if isfile(join(folder_name, f))]
    result = [extract_index_from_filename(filename) for filename in onlyfiles]
    result.sort()
    return result


def extract_index_from_filename(filename: str) -> int:
    a = re.findall('[0-9]+', filename)
    if len(a) != 1:
        print("filename:", filename, "is illegal")
        return 0
    return int(a[0])


# 假设数组应当为一个连续递增数量，返回其中缺失部分
# [1,2,3,6,7,9] -> [[4, 5], [8, 8]]
def find_gaps_in_list(index_list):
    result = []
    if len(index_list) <= 1:
        return result

    next_gap_start = 0
    found_gap = False
    for i in range(len(index_list) - 1):
        if not found_gap:
            if index_list[i] + 1 < index_list[i + 1]:
                next_gap_start = index_list[i] + 1
                found_gap = True
        else:
            result.append([next_gap_start, index_list[i] - 1])
            next_gap_start = 0
            found_gap = False
    if found_gap:
        result.append([next_gap_start, index_list[-1] - 1])
    return result


def find_missing_gaps(index_list, expected_start, expected_end):
    if len(index_list) == 0:
        return [[expected_start, expected_end]]
    result = find_gaps_in_list(index_list)
    if index_list[0] > expected_start:
        result.insert(0, [expected_start, index_list[0] - 1])
    if expected_end > index_list[-1]:
        result.append([index_list[-1] + 1, expected_end])
    return result


##################################################
# functions that help determine the current state
# of blender program
##################################################


def is_blender_running():
    for t in pygetwindow.getAllTitles():
        if t.startswith("Blender"):
            return True
    return False


def is_render_started():
    for t in pygetwindow.getAllTitles():
        if t.startswith("Blender渲染"):
            return True
    return False


##################################################
# functions that interact with blender using gui
##################################################


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
        if t.startswith("Blender") and "Blender渲染" not in t:
            return pygetwindow.getWindowsWithTitle(t)[0]
    return False


def press_render():
    # 开始渲染
    pyautogui.moveTo(121, 35)
    time.sleep(1)
    pyautogui.mouseDown()
    time.sleep(1)
    pyautogui.moveTo(121, 79, duration=0.5)
    time.sleep(1)
    pyautogui.mouseUp()
    time.sleep(1)


def render_in_foreground_range(start_frame: int, end_frame: int):
    window = find_first_blender_window()
    window.activate()
    window.maximize()
    # 等待2秒，让窗口稳定
    time.sleep(1)

    pyautogui.click(1981, 1293)
    time.sleep(0.2)
    pyautogui.click(1981, 1292)
    print("setting start frame:", start_frame)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    for s in str(start_frame):
        time.sleep(0.2)
        pyautogui.press(s)
    # pyautogui.write(str(start_frame), interval=1)
    time.sleep(0.2)
    pyautogui.press('enter')
    time.sleep(0.2)
    pyautogui.click(2077, 1294)
    time.sleep(0.2)
    pyautogui.click(2076, 1294)
    print("setting end frame:", end_frame)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    time.sleep(0.2)
    pyautogui.press("backspace")
    for s in str(end_frame):
        time.sleep(0.2)
        pyautogui.press(s)

    time.sleep(0.2)
    pyautogui.press('enter')

    time.sleep(0.2)
    while is_render_started() is False:
        press_render()



# 使用按键精灵的方式来打开blender渲染
def render_in_foreground(start_frame: int, end_frame: int, target_project: str, skip_first_init=False):
    if not skip_first_init:
        # 以命令行启动blender并打开项目
        subprocess.Popen([blender_launcher, target_project], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        # 等待20秒程序启动
        time.sleep(20)

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

    # 设定SDEF, 项目必须选定十字星后保存，不支持多人模式
    pyautogui.moveTo(321, 371)
    time.sleep(1)
    pyautogui.press("q")
    time.sleep(1) 
    pyautogui.moveTo(309, 412)
    pyautogui.click(309, 412)
    time.sleep(1)
    pyautogui.moveTo(328, 520)
    pyautogui.click(328, 520)
    time.sleep(3)
    # 设定开始和结束帧

    time.sleep(1)
    pyautogui.click(1981, 1293)
    time.sleep(1)
    pyautogui.click(1981, 1292)
    print("setting start frame:", start_frame)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
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
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    time.sleep(0.5)
    pyautogui.press("backspace")
    for s in str(end_frame):
        time.sleep(0.5)
        pyautogui.press(s)

    time.sleep(1)
    pyautogui.press('enter')

    time.sleep(1)
    while is_render_started() is False:
        press_render()


##################################################
# functions that monitoring the rendering process
##################################################


def force_render(tasks, debugging=False):
    for task in tasks:
        if "frames" in task and len(task["frames"]) > 0:
            render_frames(task)
        else:
            start_task(task, debugging)


task_status = [
    "not_started",  # 还未开始执行任务
    "ready",  # 启动blender，准备执行任务
    "rendering",  # 正在正常渲染
    "terminated",  # 渲染异常终止，blender已退出, 处理方式与not_started一样
    "frozen",  # blender未退出，但长时间没有渲染出新的结果
    "finished",  # 已渲染完成所有指定帧，blender尚未关闭
    "released",  # 已渲染完成所有指定帧，blender已经关闭
]


# return the current status and a list for next step
def check_task_status(task, last_check_timestamp, last_check_frame_number):
    frozen_timeout = 60 * 5
    blender_running = is_blender_running()
    blender_rendering = is_render_started()
    all_rendered_frames = list_all_rendered_image_index(task["target_folder"])
    remaining_gaps = find_missing_gaps(all_rendered_frames, task["start_frame"], task["end_frame"])

    if not blender_running:
        if len(remaining_gaps) == 0:
            return "released", [], time.time(), len(all_rendered_frames)
        elif len(remaining_gaps) == 1 and remaining_gaps[0][0] == task["start_frame"] and remaining_gaps[0][1] == task[
            "end_frame"]:
            return "not_started", remaining_gaps[0], time.time(), len(all_rendered_frames)
        else:
            return "terminated", remaining_gaps[0], time.time(), len(all_rendered_frames)
    else:
        if not blender_rendering:
            if len(remaining_gaps) == 0:
                return "finished", [], time.time(), len(all_rendered_frames)
            elif time.time() - last_check_timestamp > frozen_timeout and last_check_frame_number >= len(
                    all_rendered_frames):
                return "frozen", remaining_gaps[0], time.time(), len(all_rendered_frames)
            else:
                return "ready", remaining_gaps[0], time.time(), len(all_rendered_frames)
        else:
            if len(remaining_gaps) == 0:
                return "finished", [], time.time(), len(all_rendered_frames)
            elif time.time() - last_check_timestamp > frozen_timeout and last_check_frame_number >= len(
                    all_rendered_frames):
                return "frozen", remaining_gaps[0], time.time(), len(all_rendered_frames)
            else:
                return "rendering", [], time.time(), len(all_rendered_frames)


def render_frames(task):
    # assume blender is running and ready
    task["frames"].sort()
    for frame in task["frames"]:
        print("rendering: ", frame)
        filename = str(frame)
        while len(filename) < 4:
            filename = "0" + filename
        filename += ".png"
        target_file = ensure_postfix(task["target_folder"]) + filename
        if os.path.exists(target_file):
            os.remove(target_file)
        render_in_foreground_range(frame, frame)
        while not os.path.exists(target_file):
            print("wait")
            time.sleep(5)
        for t in pygetwindow.getAllTitles():
            if t.startswith("Blender渲染"):
                render_window = pygetwindow.getWindowsWithTitle(t)[0]
                render_window.close()
        time.sleep(5)
    preview_images_by_frames(ensure_postfix(task["target_folder"]),task["frames"])




def start_task(task, debugging=False):
    status, next_action, last_check_timestamp, last_check_frame_number = check_task_status(task, time.time(), -1)
    # print(status, next_action, last_check_timestamp, last_check_frame_number)
    # return
    while True:
        if debugging:
            print(status, next_action, last_check_timestamp, last_check_frame_number)
        # Full check
        if status == "not_started" or status == "terminated":
            # start blender and run render
            render_in_foreground(next_action[0], next_action[1], task["project_file"])
        elif status == "ready":
            # run render
            render_in_foreground(next_action[0], next_action[1], task["project_file"], skip_first_init=True)
        elif status == "frozen":
            close_all_blender_window()
            time.sleep(5)
            render_in_foreground(next_action[0], next_action[1], task["project_file"])
            # close blender and restart and run render
            pass
        elif status == "finished":
            # close blender and return
            close_all_blender_window()
            print("status:", status)
            return
        elif status == "released":
            print("status:", status)
            return
        elif status == "rendering":
            pass
        else:
            print("status:", status, " is invalid")

        time.sleep(10)

        # Partial Check,  60s 10 times
        for i in range(10):
            if is_blender_running():
                time.sleep(60)
            else:
                print("Partial Check Failed, Switch to full check")
                break

        # Update status
        status, next_action, last_check_timestamp, last_check_frame_number = check_task_status(task,
                                                                                               last_check_timestamp,
                                                                                               last_check_frame_number)


##################################

tasks = [
    {
        # blender 工程文件
        "project_file": "C:/myC/Personal/3DWorkSpace/Project/laugh_ningguang/bookroom_eevee.blend",
        # 图片输出路径
        "target_folder": "C:/myC/Personal/3DWorkSpace/Project/laugh_ningguang/images",
        # 起始帧
        "start_frame": 30,
        # 结束帧
        "end_frame": 2500,
        "frames": [44, 48, 58, 64, 143, 236, 241, 257, 305, 411, 629, 662, 682, 689, 869, 896, 1173, 1290, 1298, 1318, 1340, 1344, 1380, 1512, 1543, 1549, 1553, 1755, 1834, 1835, 1890, 2046, 2129, 2176, 2213, 2234, 2240, 2355, 2358, 2380, 2461]

    }
]
#
def gaps_to_framelist(gaps):
    framelist = []
    for gap in gaps:
        for i in range(gap[0], gap[1] + 1):
            framelist.append(i)
    return framelist


if __name__ == '__main__':

    force_render(tasks, debugging=True)

    # task = tasks[0]
    # all_rendered_frames = list_all_rendered_image_index(task["target_folder"])
    # remaining_gaps = find_missing_gaps(all_rendered_frames, task["start_frame"], task["end_frame"])
    # print(remaining_gaps)
    # print(gaps_to_framelist(remaining_gaps))
