# Created by 异次元学者 on 2022/5/14, 17:03

# 需要安装一些python的库
# pip install pygetwindow
# pip install pyautogui
from PIL import Image
import pygetwindow
import pyautogui
import subprocess
import time
from pynput.keyboard import Controller
import os
from os import listdir
from os.path import isfile, isdir, join
from urllib import parse
from node.vmdNode.pmx_reader import read_pmx

from common.io.file.FileManagerClient import search_files

MAGIC_PREVIEW_IMAGE_POSTFIX = "_ycyxzPreview.png"
MAGIC_PREVIEW_IMAGE_POSTFIX_GIF = "_ycyxzPreview.gif"


def get_all_filename(folder_path: str) -> list:
    return [f for f in listdir(folder_path) if isfile(join(folder_path, f))]


def get_all_subfolder(folder_path: str) -> list:
    return [f for f in listdir(folder_path) if isdir(join(folder_path, f))]


def ensure_postfix(folder_path: str):
    abs_folder_path = folder_path
    if not abs_folder_path.endswith("/"):
        abs_folder_path += "/"
    return abs_folder_path


def get_default_screenshot_name_from_file(file_path: str, postfix=MAGIC_PREVIEW_IMAGE_POSTFIX):
    if file_path.endswith(".pmx"):
        return file_path.replace(".pmx", postfix)

    if file_path.endswith(".pmd"):
        return file_path.replace(".pmd", postfix)

    if file_path.endswith(".zprj"):
        return file_path.replace(".zprj", postfix)

    if file_path.endswith(".zpac"):
        return file_path.replace(".zpac", postfix)

    if file_path.endswith(".blend"):
        return file_path.replace(".blend", postfix)

    if file_path.endswith(".vmd"):
        return file_path.replace(".vmd", postfix)
    else:
        print("Error file_path:" + file_path)
        return "screenshot.png"


def get_gif_screenshot_name_from_file(file_path: str, postfix=MAGIC_PREVIEW_IMAGE_POSTFIX_GIF):
    if file_path.endswith(".pmx"):
        return file_path.replace(".pmx", postfix)

    if file_path.endswith(".pmd"):
        return file_path.replace(".pmd", postfix)

    if file_path.endswith(".zprj"):
        return file_path.replace(".zprj", postfix)

    if file_path.endswith(".zpac"):
        return file_path.replace(".zpac", postfix)

    if file_path.endswith(".blend"):
        return file_path.replace(".blend", postfix)

    if file_path.endswith(".vmd"):
        return file_path.replace(".vmd", postfix)
    else:
        print("Error file_path:" + file_path)
        return "screenshot.gif"


# pmx_path: 需要截图的pmx文件绝对路径
def generate_screenshot_for_pmxfile(pmx_path: str, screenshot_name_convertor,
                                    pmxEditor_path="C:/Software/MMD/PmxEditor_0254f_EN - 2.0/PmxEditor_x64.exe"):
    global mouse_mover
    print("pmx/pmd文件:", pmx_path)
    path = screenshot_name_convertor(pmx_path)

    # 跳过已存在的截图文件
    if os.path.exists(path):
        print("截图文件已存在:", pmx_path)
        return

    # 需要截图的窗口标题
    screen_title = "PMXView"

    # 用PMXEditor打开Pmx文件
    process = subprocess.Popen([pmxEditor_path, pmx_path])

    # 等待，直到标题为PMXView的窗口出现
    titles = pygetwindow.getAllTitles()
    index = 0
    while screen_title not in str(titles):
        time.sleep(2)
        index += 1
        if index % 10 == 0:
            # 某些情况需要按一下回车来确认打开
            pyautogui.press('enter')
        if index > 30:
            # 出现意外情况太久(超过30*2秒)打不开模型，则直接跳过
            print("文件打开出现意外，已跳过")
            process.kill()
            return
        titles = pygetwindow.getAllTitles()

    # 获取标题为PMXView的窗口，激活并最大化
    window = pygetwindow.getWindowsWithTitle(screen_title)[0]
    window.activate()
    window.maximize()
    # 等待2秒，让模型完成显示
    time.sleep(2)

    # 记录窗口坐标
    x1, y1, x2, y2 = window.topleft.x, window.topleft.y, window.bottomright.x, window.bottomright.y

    # 点击相对坐标为20, 1396，这是PE隐藏骨骼的图标的位置，不同显示屏位置不一样， 可用pyautogui.position()查看鼠标位置，再大致转为相对位置
    # 注意不同的屏幕 坐标不一样，这里是以窗口左上角为原点，往右边的25个像素，往下移动1396个像素点，其他屏幕分辨率需要调整来找到隐藏骨骼的按钮位置
    pyautogui.click(25 + x1, 1396 + y1)
    # 点击以后移动鼠标到其他地方，防止下次失灵
    pyautogui.move((x1 + x2) // 2, (y1 + y2) // 2)

    # 等待1秒，让模型显示稳定
    time.sleep(1)

    # 根据窗口位置调整图片大小
    width = x2 - x1
    height = y2 - y1
    x1 = int(x1 + width // 4)
    x2 = int(x2 - width // 4)
    y1 = int(y1 + height // 12)
    y2 = int(y2 - height // 12)

    print(path)
    # 获取窗口位置并全屏截图
    pyautogui.screenshot(path)

    # 裁剪截图到目标大小
    im = Image.open(path)
    im = im.crop([x1, y1, x2, y2])

    # 重新保存截图
    im.save(path)
    print("完成截图:", path)

    # 退出PE以便打开下一个模型
    process.kill()

    return pmx_path, path


# pmx_path: 需要截图的pmx文件绝对路径
def generate_screenshot_for_mdfile(file_path: str, screenshot_name_convertor,
                                   exe_path="C:/Software/MarvelousDesigner10/Marvelous Designer 10 Personal/MarvelousDesigner10_Personal_x64.exe"):
    global mouse_mover
    print("md文件:", file_path)
    path = screenshot_name_convertor(file_path)

    # 跳过已存在的截图文件
    if os.path.exists(path):
        print("截图文件已存在:", file_path)
        return

    # 需要截图的窗口标题
    screen_title = "更新通知"

    # 用exe_path打开file_path文件
    process = subprocess.Popen([exe_path, file_path])

    # 等待，直到标题为PMXView的窗口出现
    titles = pygetwindow.getAllTitles()
    index = 0
    while screen_title not in str(titles):
        time.sleep(2)
        index += 1
        if index > 30:
            # 出现意外情况太久(超过30*2秒)打不开模型，则直接跳过
            print("文件打开出现意外，已跳过")
            process.kill()
            return
        titles = pygetwindow.getAllTitles()
    time.sleep(3)
    # 获取标题为PMXView的窗口，激活并最大化
    window = pygetwindow.getWindowsWithTitle(get_screen_by_title_substring(screen_title))[0]
    window.activate()
    # 点击忽略升级
    pyautogui.click(1340, 871)

    # 等待15秒模型完成显示
    time.sleep(25)
    screen_title = "Marvelous Designer"
    while screen_title not in str(titles):
        print(titles)
        time.sleep(2)
        index += 1
        if index > 30:
            # 出现意外情况太久(超过30*2秒)打不开模型，则直接跳过
            print("文件打开出现意外，已跳过")
            process.kill()
            return
        titles = pygetwindow.getAllTitles()
    # 获取MD的窗口，激活并最大化
    window = pygetwindow.getWindowsWithTitle(get_screen_by_title_substring(screen_title))[0]
    window.activate()

    print(path)
    # 获取窗口位置并全屏截图
    pyautogui.screenshot(path)

    # 裁剪截图到目标大小
    im = Image.open(path)
    im = im.crop([37, 128, 1964, 1365])

    # 重新保存截图
    im.save(path)
    print("完成截图:", path)

    # 退出PE以便打开下一个模型
    process.kill()
    return file_path, path


def get_screen_by_title_substring(screentitle):
    titles = pygetwindow.getAllTitles()
    for title in titles:
        if screentitle in title:
            return title
    print(screentitle, "not found in", titles)
    return ""


def generate_screenshot_for_file_mock(file_path: str, screenshot_name_convertor):
    return file_path, screenshot_name_convertor(file_path)


def image_template(folder: str, image_path: str, model_name: str, title="", title_link=""):
    if title_link == "":
        template = """
    <div class="gallery">
      <a target="_blank" href="{folder}">
        <img src="{image_path}" alt="404", title="{title}">
      </a>
      <h2 class="desc">{model_name}{title_link}</h2>
    </div>
    """
    else:
        template = """
        <div class="gallery">
          <a target="_blank" href="{folder}">
            <img src="{image_path}" alt="404", title="{title}">
          </a>
          <a href="{title_link}" target="_blank"><h2 class="desc">{model_name}</h2></a>
        </div>
        """
    return template.format(folder=parse.quote(folder), image_path=parse.quote(image_path), model_name=model_name,
                           title=title, title_link=title_link)


def html_template(content: str, image_width=600, image_height="auto"):
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
    if image_height != "auto":
        image_height = str(image_height) + "px"
    return template.replace("{content}", content).replace("{image_width}", str(image_width) + "px").replace(
        "{image_height}", image_height)


def to_relative_path(reference_abs_path: str, abs_path: str):
    return abs_path.replace(reference_abs_path, "")


def to_model_name(model_path: str):
    if "/" in model_path:
        return model_path[model_path.rindex("/") + 1:model_path.rindex(".")]
    else:
        return model_path[: int(model_path.rindex("."))]


def to_parent_folder(path: str):
    if "/" in path:
        return path[0: path.rindex("/") + 1]
    else:
        return "./"


# windows 路径去除反斜杠
def ensure_path_format(filepath: str):
    while "\\" in filepath:
        filepath = filepath.replace("\\", "/")
    return filepath


def screenshot_for_mmd_models():
    # PMXEditor的安装路径
    pmxEditor_path = ensure_path_format("C:/Software/MMD/PmxEditor_0254f_EN - 2.0/PmxEditor_x64.exe")

    # 要处理的文件夹目录
    root = ensure_path_format("C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Model/external/human")

    all_pmx_file = search_files(root, {".pmx", ".pmd"})
    print("共找到", len(all_pmx_file), "个模型文件")

    # 生成截图
    for file in all_pmx_file:
        generate_screenshot_for_pmxfile(file, get_default_screenshot_name_from_file,
                                        pmxEditor_path)

    # 生成html文件用于预览上面生成的截图， 如果已经有截图了可以跳过上面的步骤直接生成html
    all_model_relative_path = []
    all_image_relative_path = []
    for file in all_pmx_file:
        pmx_path, path = generate_screenshot_for_file_mock(file, get_default_screenshot_name_from_file)
        rel_pmx_path = to_relative_path(root, pmx_path)
        rel_image_path = to_relative_path(root, path)

        all_model_relative_path.append(rel_pmx_path)
        all_image_relative_path.append(rel_image_path)

        print(rel_image_path, rel_pmx_path)
        print(to_model_name(rel_pmx_path))

    all_image_component = []
    for i in range(len(all_model_relative_path)):
        image_component = image_template("." + to_parent_folder(all_model_relative_path[i]),
                                         "." + all_image_relative_path[i], to_model_name(all_model_relative_path[i]))
        all_image_component.append(image_component)
    image_content = "\n".join(all_image_component)

    html = html_template(image_content, image_width=600, image_height=600)

    with open(ensure_postfix(root) + "previewAll.html", mode="w", encoding="utf-8") as f:
        f.write(html)


def screenshot_for_md():
    # PMXEditor的安装路径
    md_path = ensure_path_format(
        "C:/Software/MarvelousDesigner10/Marvelous Designer 10 Personal/MarvelousDesigner10_Personal_x64.exe")

    # 要处理的文件夹目录
    root = ensure_path_format("C:/Software/MarvelousDesigner10/Models")

    all_md_file = search_files(root, {".zprj", ".zpac"})
    print("共找到", len(all_md_file), "个模型文件")

    # all_md_file = all_md_file[0:1]

    # 生成截图
    for file in all_md_file:
        generate_screenshot_for_mdfile(file, get_default_screenshot_name_from_file,
                                       md_path)

    # 生成html文件用于预览上面生成的截图， 如果已经有截图了可以跳过上面的步骤直接生成html
    all_model_relative_path = []
    all_image_relative_path = []
    for file in all_md_file:
        pmx_path, path = generate_screenshot_for_file_mock(file, get_default_screenshot_name_from_file)
        rel_pmx_path = to_relative_path(root, pmx_path)
        rel_image_path = to_relative_path(root, path)

        all_model_relative_path.append(rel_pmx_path)
        all_image_relative_path.append(rel_image_path)

        print(rel_image_path, rel_pmx_path)
        print(to_model_name(rel_pmx_path))

    all_image_component = []
    for i in range(len(all_model_relative_path)):
        image_component = image_template("." + to_parent_folder(all_model_relative_path[i]),
                                         "." + all_image_relative_path[i], to_model_name(all_model_relative_path[i]))
        all_image_component.append(image_component)
    image_content = "\n".join(all_image_component)

    html = html_template(image_content, image_width=900, image_height=600)

    with open(ensure_postfix(root) + "previewAll.html", mode="w", encoding="utf-8") as f:
        f.write(html)


# 不要输入一个很长的数组，太长会报错
def render_frames(frames: list, target_project: str, save_path: str,
                  blender_cmd="C:/Software/blender3/Windows/Release/blender.exe"):
    if "assets" in save_path:
        print("skipping assert file", save_path)
        return
    path_with_frame = save_path.replace("png", "png0000.png")
    path_with_frame_jpg = save_path.replace("png", "png0000.jpg")
    if os.path.exists(path_with_frame):
        print("renamed:", path_with_frame)
        os.rename(path_with_frame, save_path)
    if os.path.exists(path_with_frame_jpg):
        print("renamed:", path_with_frame_jpg)
        os.rename(path_with_frame_jpg, save_path)
    # 跳过已存在的截图文件
    if os.path.exists(save_path):
        print("截图文件已存在:", save_path)
        return

    frame_str = ",".join(map(str, frames))
    process = subprocess.Popen([blender_cmd, target_project, "-o", save_path, "-E", "BLENDER_EEVEE", "-f", frame_str],
                               stdout=subprocess.DEVNULL)

    path_with_frame = save_path.replace("png", "png0000.png")
    for i in range(60 * 5):
        time.sleep(2)
        if os.path.exists(path_with_frame) or os.path.exists(path_with_frame_jpg):
            break
        print("Waiting for", save_path)
    if os.path.exists(path_with_frame):
        process.kill()
        time.sleep(2)
        os.rename(path_with_frame, save_path)
    elif os.path.exists(path_with_frame_jpg):
        process.kill()
        time.sleep(2)
        os.rename(path_with_frame_jpg, save_path)
    else:
        # 使用截图代替渲染，如果项目中没有相机有可能导致无法渲染
        w = get_screen_by_title_substring(".blend")
        if w != "":
            window = pygetwindow.getWindowsWithTitle(get_screen_by_title_substring(".blend"))[0]
            window.activate()
            pyautogui.screenshot(save_path)
            time.sleep(2)
        process.kill()


def screenshot_for_blend():
    # PMXEditor的安装路径
    blender_path = ensure_path_format(
        "C:/Software/blender3/Windows/Release/blender.exe")

    # 要处理的文件夹目录
    root = ensure_path_format("C:/myC/Personal/3DWorkSpace/Stages")

    all_blend_file = search_files(root, {".blend"})
    print("共找到", len(all_blend_file), "个模型文件")

    # all_blend_file = all_blend_file[0:1]
    # 生成截图
    for file in all_blend_file:
        print(get_default_screenshot_name_from_file(file))
        render_frames([0], file, get_default_screenshot_name_from_file(file),
                      blender_path)

        # 生成html文件用于预览上面生成的截图， 如果已经有截图了可以跳过上面的步骤直接生成html
    all_model_relative_path = []
    all_image_relative_path = []
    for file in all_blend_file:
        pmx_path, path = generate_screenshot_for_file_mock(file, get_default_screenshot_name_from_file)
        rel_pmx_path = to_relative_path(root, pmx_path)
        rel_image_path = to_relative_path(root, path)

        all_model_relative_path.append(rel_pmx_path)
        all_image_relative_path.append(rel_image_path)

        print(rel_image_path, rel_pmx_path)
        print(to_model_name(rel_pmx_path))

    all_image_component = []
    for i in range(len(all_model_relative_path)):
        image_component = image_template("." + to_parent_folder(all_model_relative_path[i]),
                                         "." + all_image_relative_path[i], to_model_name(all_model_relative_path[i]))
        all_image_component.append(image_component)
    image_content = "\n".join(all_image_component)

    html = html_template(image_content, image_width=900, image_height=600)

    with open(ensure_postfix(root) + "previewAll.html", mode="w", encoding="utf-8") as f:
        f.write(html)


def expected_load_time(file_size):
    return int(30 / 55139761 * file_size + 3)


def screenshot_for_vmd():
    # 要处理的文件夹目录
    root = ensure_path_format("C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/external")

    all_vmd_file = search_files(root, {".vmd"})

    motion_file = []
    for file in all_vmd_file:
        # 只处理大于100KB的文件, cnv修复文件由于重复，不需要
        s = os.path.getsize(file)
        if s > 100000 and "-cnv" not in file and "表情" not in file and "視線" not in file and "camera" not in str(file).lower() and "cam" not in str(file).lower() and "镜头" not in file:
            motion_file.append(file)
    print("共找到", len(motion_file), "个动作文件")
    #
    for file in motion_file:
        try:
            render_vmd_frames(file)
        except Exception:
            continue

    file_per_html = 200
    index = 0
    while len(motion_file) > file_per_html:
        current_motion_file = motion_file[0: file_per_html]
        motion_file = motion_file[file_per_html:]
        all_model_relative_path = []
        all_image_relative_path = []
        for file in current_motion_file:
            pmx_path, path = generate_screenshot_for_file_mock(file, get_gif_screenshot_name_from_file)
            rel_pmx_path = to_relative_path(root, pmx_path)
            rel_image_path = to_relative_path(root, path)

            all_model_relative_path.append(rel_pmx_path)
            all_image_relative_path.append(rel_image_path)

            # print(rel_image_path, rel_pmx_path)
            # print(to_model_name(rel_pmx_path))

        all_image_component = []

        for i in range(len(all_model_relative_path)):
            music = get_music_available_for_motion(current_motion_file[i])
            if music != "":
                music = "." + to_parent_folder(all_model_relative_path[i]) + music

            image_component = image_template("." + to_parent_folder(all_model_relative_path[i]),
                                             "." + all_image_relative_path[i],
                                             to_model_name(all_model_relative_path[i]),
                                             title="时长: " + str(read_pmx(current_motion_file[i])[1] // 30) + " s",
                                             title_link=music)
            all_image_component.append(image_component)
        image_content = "\n".join(all_image_component)

        html = html_template(image_content, image_width=900, image_height=600)

        with open(ensure_postfix(root) + "previewAll" + str(index) + ".html", mode="w", encoding="utf-8") as f:
            f.write(html)
        index += 1

    current_motion_file = motion_file
    all_model_relative_path = []
    all_image_relative_path = []
    for file in current_motion_file:
        pmx_path, path = generate_screenshot_for_file_mock(file, get_gif_screenshot_name_from_file)
        rel_pmx_path = to_relative_path(root, pmx_path)
        rel_image_path = to_relative_path(root, path)

        all_model_relative_path.append(rel_pmx_path)
        all_image_relative_path.append(rel_image_path)

        # print(rel_image_path, rel_pmx_path)
        # print(to_model_name(rel_pmx_path))

    all_image_component = []

    for i in range(len(all_model_relative_path)):
        music = get_music_available_for_motion(current_motion_file[i])
        if music != "":
            music = "." + to_parent_folder(all_model_relative_path[i]) + music

        image_component = image_template("." + to_parent_folder(all_model_relative_path[i]),
                                         "." + all_image_relative_path[i],
                                         to_model_name(all_model_relative_path[i]),
                                         title="时长: " + str(read_pmx(current_motion_file[i])[1] // 30) + " s",
                                         title_link=music)
        all_image_component.append(image_component)
    image_content = "\n".join(all_image_component)

    html = html_template(image_content, image_width=900, image_height=600)

    with open(ensure_postfix(root) + "previewAll" + str(index) + ".html", mode="w", encoding="utf-8") as f:
        f.write(html)


def render_vmd_frames(file: str):
    target_output_file = get_default_screenshot_name_from_file(file, MAGIC_PREVIEW_IMAGE_POSTFIX_GIF)
    if os.path.exists(target_output_file):
        print("already exist:", target_output_file)
        return
    start_time = time.time()
    # 渲染出一系列的jpg文件并合成为gif

    # MMD的安装路径
    mmd_path = ensure_path_format(
        "C:/Software/MMD/MikuMikuDanceE_v932x64/MikuMikuDance.exe")

    # 渲染使用的默认人物模型
    default_model_path = "C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Model/external/human/精选/iRon0129/西装短裙-黑丝-Haku"
    default_model_name = "Tda Uniform Haku 2.1 by iRon0129.pmx"

    # 缩略gif的起始帧, 间隔帧, 帧数, gif每一帧的时间压缩比例
    default_start = 100
    default_interval = 10
    default_frame = 60
    gif_acc_index = 3

    # for short motion, optimize
    max_frame = read_pmx(file)[1]
    if max_frame > 0:
        if max_frame < default_start + default_interval * default_frame:
            default_start = 0
            default_interval = max_frame // 60
            default_frame = 60

    buffer_folder = ensure_path_format("C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/")
    target_frames = [default_start + i * default_interval for i in range(default_frame)]
    filenames = [buffer_folder + str(default_start + i * default_interval) + ".jpg" for i in range(default_frame)]

    # 注意不要开启中文输入法
    keyboard = Controller()

    # 开启MMD
    # 用PMXEditor打开Pmx文件
    process = subprocess.Popen([mmd_path])
    wait_for_window("MikuMikuDance")

    # 获取标题为MikuMikuDance的窗口，激活并最大化
    window = pygetwindow.getWindowsWithTitle("MikuMikuDance")[0]
    window.activate()
    window.maximize()

    # 加载模型
    model_load_button_x, model_load_button_y = 252, 1294
    pyautogui.click(model_load_button_x, model_load_button_y)
    time.sleep(1)
    handle_mmd_loading_dialog(default_model_path, default_model_name, keyboard)
    time.sleep(1)
    # 确定加载
    pyautogui.press('enter')

    # 加载动作
    time.sleep(2)
    menu_x, menu_y = 17, 31
    motion_load_button_x, motion_load_button_y = 98, 248

    pyautogui.click(menu_x, menu_y)
    time.sleep(1)
    pyautogui.click(motion_load_button_x, motion_load_button_y)
    time.sleep(1)

    handle_mmd_loading_dialog(file[0:file.rindex("/")], file[file.rindex("/") + 1:], keyboard)
    time.sleep(1)
    # 确定加载
    pyautogui.press('enter')
    time.sleep(expected_load_time(os.path.getsize(file)))

    # 隐藏骨骼显示
    pyautogui.click(389, 1269)

    frame_input_x, frame_input_y = 399, 120
    for frame in target_frames:
        # loop
        # 设置关键帧
        pyautogui.click(frame_input_x, frame_input_y)
        pyautogui.hotkey("ctrl", "a")
        pyautogui.press("bcakspace")
        pyautogui.press("bcakspace")
        pyautogui.press("bcakspace")
        pyautogui.press("bcakspace")
        keyboard.type(str(frame))
        pyautogui.press("enter")
        time.sleep(0.5)

        # 渲染

        # 截图方案
        path = buffer_folder + str(frame) + ".jpg"
        pyautogui.screenshot(path)
        # 裁剪截图到目标大小
        im = Image.open(path)
        im = im.crop([770, 164, 2557, 1109])
        # 重新保存截图
        im.save(path)
        # 渲染方案
        # render_pic_button_x, render_pic_button_y = 101, 172
        # pyautogui.click(menu_x, menu_y)
        # time.sleep(1)
        # pyautogui.click(render_pic_button_x, render_pic_button_y)
        #
        # handle_mmd_rendering_dialog(buffer_folder, str(frame) + ".jpg", keyboard)
        # time.sleep(2)

    # 关闭MMD
    process.kill()
    make_gif(filenames, get_default_screenshot_name_from_file(file, MAGIC_PREVIEW_IMAGE_POSTFIX_GIF),
             default_interval * 33 // gif_acc_index)
    print("time: ", time.time() - start_time)


def wait_for_window(window_name, interval=2, timeout=10):
    # 等待，直到标题为PMXView的窗口出现
    titles = pygetwindow.getAllTitles()
    for i in range(timeout):
        if window_name in str(titles):
            return
        else:
            time.sleep(interval)
            titles = pygetwindow.getAllTitles()


def handle_mmd_loading_dialog(path, name, keyboard):
    dialog_path_input_x, dialog_path_input_y = 2026, 338
    dialog_name_input_x, dialog_name_input_y = 1313, 1150
    pyautogui.click(dialog_path_input_x, dialog_path_input_y)
    pyautogui.press('backspace')
    keyboard.type(path)
    pyautogui.press('enter')
    time.sleep(1)

    pyautogui.click(dialog_name_input_x, dialog_name_input_y)
    pyautogui.press('backspace')
    keyboard.type(name)
    time.sleep(1)
    pyautogui.press('enter')


def handle_mmd_rendering_dialog(path, name, keyboard):
    dialog_path_input_x, dialog_path_input_y = 2016, 339
    dialog_name_input_x, dialog_name_input_y = 2209, 1105
    pyautogui.click(dialog_path_input_x, dialog_path_input_y)
    pyautogui.press('backspace')
    keyboard.type(path)
    pyautogui.press('enter')
    time.sleep(1)

    pyautogui.click(dialog_name_input_x, dialog_name_input_y)
    pyautogui.press('backspace')
    keyboard.type(name)
    time.sleep(1)
    pyautogui.press('enter')


def get_music_available_for_motion(file):
    parent_folder = to_parent_folder(file)
    all_file_name = str()

    for file_name in get_all_filename(parent_folder):
        if ".wav" in file_name or ".mp3" in file_name:
            return file_name
    return ""


def make_gif(source, out, duration):
    frames = [Image.open(image) for image in source]
    frame_one = frames[0]
    frame_one.save(out, format="GIF", append_images=frames,
                   save_all=True, duration=duration, loop=0)
    print("finish:" + out)
    # 清空文件
    for file in source:
        os.remove(file)

if __name__ == '__main__':
    # # 为所有MMD人物模型创建预览图
    screenshot_for_mmd_models()

    # # 为所有场景创建预览图
    screenshot_for_blend()
    #
    # # 为所有布料模型创建预览图
    screenshot_for_md()
    #
    # 为所有动作创建gif预览图
    # screenshot_for_vmd()

    # make_gif(['C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/500.jpg',
    #           'C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/530.jpg',
    #           'C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/560.jpg',
    #           'C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/590.jpg',
    #           'C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/620.jpg',
    #           'C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/buffer/650.jpg']
    #          , "./test.gif", 500)
