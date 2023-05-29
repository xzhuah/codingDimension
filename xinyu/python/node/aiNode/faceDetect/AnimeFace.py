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
from anime_face_detector import create_detector


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


def face_detect2(image_path, detector="yolov3"):
    detector = create_detector(detector)
    image = load_image(image_path)
    preds = detector(image)
    print(preds[0])

    print(len(preds))


def add_facebox(image, detector, score_threshold=0):
    result = detect_faces_with_anime_face_detector(image, detector)
    # 遍历所有检测到的动漫脸
    max_score = -1
    for face in result:
        if face["score"] > score_threshold:
            image = cv2.rectangle(image, (face["box"][0], face["box"][1]), (face["box"][2], face["box"][3]),
                                  (255, 0, 255), 5)  # 绘制矩形框
        max_score = max(face["score"], max_score)
    return image, max_score


def detect_faces_with_anime_face_detector(image, detector) -> list:
    result = []
    preds = detector(image)
    for pred in preds:
        result.append({
            "box": [int(pred["bbox"][0]), int(pred["bbox"][1]), int(pred["bbox"][2]), int(pred["bbox"][3])],
            "score": float(pred["bbox"][4])
        })
    # result.sort(key=lambda i: i["score"], reverse=True)
    return result


def detect_faces_with_lbpcascade(image, face_cascade) -> list:
    result = []
    img_gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)  # 图片灰度化
    img_gray = cv2.equalizeHist(img_gray)  # 直方图均衡化
    faces = face_cascade.detectMultiScale(img_gray)  # 多尺度
    for (x, y, w, h) in faces:
        result.append({
            "box": [x, y, x + w, y + h]
        })
    return result


class AnimeFaceProcessor(BaseFileProcessor):

    def __init__(self):
        super(AnimeFaceProcessor, self).__init__()
        self.cache_file_postfix = "_ycyxzCache.json"
        # self.cascade_name = os.path.join(project_input_root, "lbpcascade_animeface.xml")
        # self.face_cascade = cv2.CascadeClassifier(self.cascade_name)  # 加载级联分类器
        self.anime_face_detector = create_detector('yolov3')
        self.score_threshold = 0.5
        self.use_cache = True
        self.draw_box = False

    def filepath_filter(self, filepath: str) -> bool:
        return filepath.endswith("_ycyxzPreview.png")

    def process_file(self, filepath: str):
        dir_name = os.path.dirname(filepath)
        base_name = os.path.basename(filepath)

        cache_file_path = os.path.join(dir_name, base_name.replace("_ycyxzPreview.png", self.cache_file_postfix))
        if self.use_cache and os.path.exists(cache_file_path):
            return json.loads(read_file(cache_file_path))["face_num"]

        logging = {
            "model": base_name,
            "create_time": time.time()
        }

        # # method 1 Deprecated, method 2 is significantly better, method 1 is for reference
        # image = load_image(filepath)
        # result = detect_faces_with_lbpcascade(image, self.face_cascade)
        # # 遍历所有检测到的动漫脸
        # for face in result:
        #     image = cv2.rectangle(image, (face["box"][0], face["box"][1]), (face["box"][2], face["box"][3]),
        #                           (255, 0, 255), 5)  # 绘制矩形框
        # face_num = len(result)
        # face_result = 1 if face_num == 1 else 0
        # write_image(os.path.join(dir_name, base_name.replace("_ycyxzPreview.png",
        #                                                      "_ycyxzLFace{face_num}.png".format(face_num=face_result))),
        #             image)
        # logging["lbpcascade_face_nume"] = face_num

        # method 2
        image = load_image(filepath)
        result = detect_faces_with_anime_face_detector(image, self.anime_face_detector)
        # 遍历所有检测到的动漫脸
        max_score = -1
        for face in result:
            if face["score"] > self.score_threshold:
                if self.draw_box:
                    image = cv2.rectangle(image, (face["box"][0], face["box"][1]), (face["box"][2], face["box"][3]),
                                          (255, 0, 255), 5)  # 绘制矩形框
                #
            max_score = max(face["score"], max_score)
        face_num = len(result)
        face_result = 1 if max_score > self.score_threshold else 0
        write_image(os.path.join(dir_name, base_name.replace("_ycyxzPreview.png",
                                                             "_ycyxzAFace{face_num}.png".format(face_num=face_result))),
                    image)

        logging["face_num"] = face_num
        logging["score"] = max_score

        wrtie_file(cache_file_path, json.dumps(logging))
        return face_num

    def batch_process(self, root: str):
        result = self.process_to_map(root)
        # previewToWebpage0 = YcyxzPreviewToWebpage(postfix="_ycyxzLFace0.png", source_postfix=".pmx",
        #                                           max_batch_size=1600)
        # previewToWebpage1 = YcyxzPreviewToWebpage(postfix="_ycyxzLFace1.png", source_postfix=".pmx",
        #                                           max_batch_size=1600)
        # previewToWebpage0.generate_webpage(root, html_prefix="negative_Lface")
        # previewToWebpage1.generate_webpage(root, html_prefix="positive_Lface")

        previewToWebpage2 = YcyxzPreviewToWebpage(postfix="_ycyxzAFace0.png", source_postfix=".pmx",
                                                  max_batch_size=1600)
        previewToWebpage3 = YcyxzPreviewToWebpage(postfix="_ycyxzAFace1.png", source_postfix=".pmx",
                                                  max_batch_size=1600)
        previewToWebpage2.generate_webpage(root, html_prefix="negative_Aface")
        previewToWebpage3.generate_webpage(root, html_prefix="positive_Aface")
        return result


class BatchFileRemover(BaseFileProcessor):
    def filepath_filter(self, filepath: str) -> bool:
        return filepath.endswith("_ycyxzAFace0.png") or filepath.endswith("_ycyxzAFace1.png") or filepath.endswith(
            "_ycyxzLFace0.png") or filepath.endswith("_ycyxzLFace1.png")

    def process_file(self, filepath: str):
        os.remove(filepath)


if __name__ == '__main__':
    # cuda0 = torch.device('cuda:0')
    # print(torch.__version__)
    # print(torch.version.cuda)
    # face_detect(
    #     "D:/Work/3DWorkspace/MMDResource/human/精选/Vsinger官方/洛天依V4公式服_by_Vsinger团队/洛天依V4公式服/luotianyi_v4_ver3.3_ycyxzPreview.png",
    #     os.path.join(project_input_root, "lbpcascade_animeface.xml"))

    # face_detect2(
    #     "D:/Work/3DWorkspace/MMDResource/human/精选/Vsinger官方/洛天依V4公式服_by_Vsinger团队/洛天依V4公式服/luotianyi_v4_ver3.3_ycyxzPreview.png")

    root = "D:/Work/3DWorkspace/MMDResource/human/"
    # root = "D:/Work/3DWorkspace/MMDResource/human/精选/TDA改有完整素体/Nonstop Pack_marsissey"
    # root = "D:/Work/3DWorkspace/MMDResource/human/精选/Vsinger官方"
    #
    anime_face_processor = AnimeFaceProcessor()
    result = anime_face_processor.batch_process(root)
    anime_face_processor.profiling_summary()

    # counter = defaultdict(list)
    # for key in result:
    #     counter[result[key]].append(key)
    #
    # for key in counter:
    #     print(key, len(counter[key]))
    #
    # for key in counter:
    #     if key != 1:
    #         print("incorrect face_num:", key)
    #         pprint(counter[key])

    # cache_remover = BatchFileRemover()
    # result = cache_remover.process_to_list(root)
    # cache_remover.profiling_summary()
    # print(len(result))
