# Created by Xinyu Zhu on 1/3/2023, 12:43 AM

import cv2
import json
import numpy as np
from common.io.file import project_input_root
from common.io.file.PlainTextClient import wrtie_file
from common.io.file.BatchFileClient import BaseFileProcessor, YcyxzPreviewToWebpage
import os
import time
from collections import defaultdict
from pprint import pprint


def read_file(filename) -> str:
    with open(filename, encoding='utf-8') as f:
        return f.read()


# opencv 不支持特殊字符文件路径
def load_image(image_path):
    return cv2.imdecode(np.fromfile(image_path, dtype=np.uint8), cv2.IMREAD_UNCHANGED)


def write_image(image_path, image):
    is_success, im_buf_arr = cv2.imencode(".png", image)
    im_buf_arr.tofile(image_path)


# 检测二次元面部算法
def face_detect(image_path, cascade_name):
    image = load_image(image_path)
    img_gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)  # 图片灰度化
    img_gray = cv2.equalizeHist(img_gray)  # 直方图均衡化
    face_cascade = cv2.CascadeClassifier(cascade_name)  # 加载级联分类器
    faces = face_cascade.detectMultiScale(img_gray)  # 多尺度检测
    for (x, y, w, h) in faces:  # 遍历所有检测到的动漫脸
        image = cv2.rectangle(image, (x, y), (x + w, y + h), (255, 0, 255), 5)  # 绘制矩形框
    print(len(faces), faces)

    window_name = 'Face detection'
    cv2.namedWindow(window_name)  # Create a named window
    cv2.moveWindow(window_name, 40, 30)  # Move it to (40,30)
    cv2.imshow(window_name, image)  # 检测效果预览
    cv2.waitKey(0)  # 保持窗口显示


class AnimeFaceProcessor(BaseFileProcessor):

    def __init__(self):
        super(AnimeFaceProcessor, self).__init__()
        self.cache_file_postfix = "_ycyxzCache.json"
        self.cascade_name = os.path.join(project_input_root, "lbpcascade_animeface.xml")

    def filepath_filter(self, filepath: str) -> bool:
        return filepath.endswith("_ycyxzPreview.png")

    def process_file(self, filepath: str):
        dir_name = os.path.dirname(filepath)
        base_name = os.path.basename(filepath)

        cache_file_path = os.path.join(dir_name, base_name.replace("_ycyxzPreview.png", self.cache_file_postfix))
        if os.path.exists(cache_file_path):
            return json.loads(read_file(cache_file_path))["face_num"]

        image = load_image(filepath)
        img_gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)  # 图片灰度化
        img_gray = cv2.equalizeHist(img_gray)  # 直方图均衡化
        face_cascade = cv2.CascadeClassifier(self.cascade_name)  # 加载级联分类器
        faces = face_cascade.detectMultiScale(img_gray)  # 多尺度检测
        for (x, y, w, h) in faces:  # 遍历所有检测到的动漫脸
            image = cv2.rectangle(image, (x, y), (x + w, y + h), (255, 0, 255), 5)  # 绘制矩形框

        face_num = len(faces)
        face_result = 1 if face_num == 1 else 0
        base_name = base_name.replace("_ycyxzPreview.png", "_ycyxzFace{face_num}.png".format(face_num=face_result))

        write_image(os.path.join(dir_name, base_name), image)
        wrtie_file(cache_file_path, json.dumps({
            "model": base_name,
            "face_num": face_num,
            "create_time": time.time()
        }))
        return face_num

    def batch_process(self, root: str):
        result = self.process_to_map(root)
        previewToWebpage0 = YcyxzPreviewToWebpage(postfix="_ycyxzFace0.png", source_postfix=".pmx", max_batch_size=800)
        previewToWebpage1 = YcyxzPreviewToWebpage(postfix="_ycyxzFace1.png", source_postfix=".pmx", max_batch_size=800)
        previewToWebpage0.generate_webpage(root, html_prefix="negative_face")
        previewToWebpage1.generate_webpage(root, html_prefix="positive_face")
        return result


if __name__ == '__main__':
    # face_detect(
    #     "D:/Work/3DWorkspace/MMDResource/human/精选/Vsinger官方/洛天依V4公式服_by_Vsinger团队/洛天依V4公式服/luotianyi_v4_ver3.3_ycyxzPreview.png",
    #     os.path.join(project_input_root, "lbpcascade_animeface.xml"))

    root = "D:/Work/3DWorkspace/MMDResource/human/"

    anime_face_processor = AnimeFaceProcessor()
    result = anime_face_processor.batch_process(root)
    anime_face_processor.profiling_summary()

    counter = defaultdict(list)
    for key in result:
        counter[result[key]].append(key)

    for key in counter:
        print(key, len(counter[key]))

    for key in counter:
        if key != 1:
            print("incorrect face_num:", key)
            pprint(counter[key])
