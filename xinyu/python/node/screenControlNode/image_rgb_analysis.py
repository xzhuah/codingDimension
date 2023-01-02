# Created by Xinyu Zhu on 2022/8/11, 19:11

from PIL import Image
from numpy import asarray
import numpy as np
from os import listdir
from os.path import isfile, join
import threading
import re
import glob
from urllib import parse
import time


###########################################

def image_template(image_path: str):
    template = """
<div class="gallery">
  <a target="_blank" href="{folder}">
    <img src="{image_path}" alt="404">
  </a>
  <h2 class="desc">{model_name}</h2>
</div>
"""
    return template.format(folder=parse.quote(to_parent_folder(image_path)), image_path=parse.quote(image_path),
                           model_name=to_model_name(image_path))


def image_template(image_path: str):
    template = """
<div class="gallery">
  <a target="_blank" href="{folder}">
    <img src="{image_path}" alt="404">
  </a>
  <h2 class="desc">{model_name}</h2>
</div>
"""
    return template.format(folder=parse.quote(to_parent_folder(image_path)), image_path=parse.quote(image_path),
                           model_name=to_model_name(image_path))


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


# windows 路径去除反斜杠
def ensure_path_format(filepath: str):
    while "\\" in filepath:
        filepath = filepath.replace("\\", "/")
    return filepath


def ensure_postfix(folder_path: str):
    abs_folder_path = folder_path
    if not abs_folder_path.endswith("/"):
        abs_folder_path += "/"
    return abs_folder_path


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


def to_relative_path(reference_abs_path: str, abs_path: str):
    return abs_path.replace(reference_abs_path, "")


def preview_image_batch(root, image_paths, output_filename="preview.html"):
    image_components = []
    for pic_file in image_paths:
        image_components.append(image_template2(to_relative_path(root, pic_file)))
    image_content = "\n".join(image_components)
    index = 2
    html = html_template2(image_content, image_width=80 * index, image_height=160 * index)
    with open(ensure_postfix(root) + output_filename, mode="w", encoding="utf-8") as f:
        f.write(html)
    print(len(image_paths))


###############################################


def distance(file_mean, all_mean):
    return abs(file_mean[0] - all_mean[0]) + abs(file_mean[1] - all_mean[1]) + abs(file_mean[2] - all_mean[2])


def extract_index_from_filename(filename: str) -> int:
    a = re.findall('[0-9]+', filename)
    if len(a) != 1:
        print("filename:", filename, "is illegal")
        return 0
    return int(a[0])


def load_images(root):
    filelist = glob.glob(root + "*.png")
    return np.array([np.array(Image.open(fname)) for fname in filelist])


def index_to_filename(image_index):
    filename = str(image_index)
    while len(filename) < 4:
        filename = "0" + filename
    return filename + ".png"


def read_image(filename: str, processor, callback_dict: dict):
    image = Image.open(filename)
    result = processor(asarray(image))
    callback_dict[filename[filename.rindex("/")+1:]] = result
    return result



# in order
def read_images(filenames, processor, callback_dict):
    for filename in filenames:
        read_image(filename, processor, callback_dict)


# multi-thread file reader
def read_image_parallel(filenames, processor, batch_size=10):
    batch_num = len(filenames) // batch_size
    result = []
    for batch in range(batch_num):
        result.append(filenames[batch * batch_size: (batch + 1) * batch_size])

    if len(filenames) % batch_size != 0:
        result.append(filenames[batch_num * batch_size:])

    callback_dict = {}
    all_tasks = [threading.Thread(target=read_images, args=(filebatch, processor, callback_dict)) for filebatch in
                 result]
    [task.start() for task in all_tasks]
    [task.join() for task in all_tasks]
    return callback_dict


def get_batch_size(element_num, expected_batch_num):
    return element_num // expected_batch_num


def image_process(data):
    return {
        "rgb_mean": np.mean(data, axis=(0, 1))
    }


if __name__ == '__main__':
    recall = 150
    root = "C:/myC/Personal/3DWorkSpace/Project/primaryStar_Ying/image/"
    onlyfiles = [root + f for f in listdir(root) if isfile(join(root, f))]
    all_image = []
    for file in onlyfiles:
        if file.endswith(".png"):
            all_image.append(file)

    # all_image = all_image[0:300]

    start_time = time.time()
    result = read_image_parallel(all_image, image_process, get_batch_size(len(onlyfiles), 300))
    print(result)
    print(time.time() - start_time)

    all_mean_data = []
    for key in result:
        all_mean_data.append(result[key]["rgb_mean"])
    all_mean = np.mean(all_mean_data, axis=0)

    all_dist_to_mean = {}
    for key in result:
        all_dist_to_mean[key] = distance(result[key]["rgb_mean"], all_mean)

    sorted_distance_to_mean = sorted(all_dist_to_mean.items(), key=lambda kv: kv[1], reverse=True)
    print("sorted_distance_to_mean:")
    print(sorted_distance_to_mean[0:recall])
    preview_image_batch(root, [root + image_file_name[0] for image_file_name in sorted_distance_to_mean[0:recall]],
                        "avg_distance.html")

    sorted_key = sorted(result.keys())

    sibling_distance = {}
    for index in range(len(sorted_key)):
        d = 0
        num = 0
        if index - 1 >= 0:
            d += distance(result[sorted_key[index]]["rgb_mean"], result[sorted_key[index - 1]]["rgb_mean"])
            num += 1
        if index + 1 < len(sorted_key):
            d += distance(result[sorted_key[index]]["rgb_mean"], result[sorted_key[index + 1]]["rgb_mean"])
            num += 1
        sibling_distance_value = d / num
        sibling_distance[sorted_key[index]] = sibling_distance_value

    sorted_sibling_distance_to_mean = sorted(sibling_distance.items(), key=lambda kv: kv[1], reverse=True)
    print("sorted_sibling_distance_to_mean:")
    print(sorted_sibling_distance_to_mean[0:recall])

    preview_image_batch(root, [root + image_file_name[0] for image_file_name in
                               sorted_sibling_distance_to_mean[0:recall]],
                        "sibling_distance.html")


#
# if __name__ == '__main__':
#     root = "C:/myC/Personal/3DWorkSpace/Project/foxlalala/image/"
#     onlyfiles = [f for f in listdir(root) if isfile(join(root, f))]
#
#     recall = 150
#     all_file_mean = []
#     all_file_name = []
#
#     file_to_mean = {}
#     # test = 10
#     for file in onlyfiles:
#         if file.endswith(".png"):
#             image = Image.open(root + file)
#             data = asarray(image)
#             rgb_mean = np.mean(data, axis=(0, 1))  # (r, g, b, a)
#             all_file_mean.append(rgb_mean)
#             all_file_name.append(file)
#             file_index = extract_index_from_filename(file)
#             file_to_mean[file_index] = rgb_mean
#             # test -= 1
#             # if test < 0:
#             #     break
#             if file_index % 10 == 0:
#                 print(file)
#     all_mean = np.mean(all_file_mean, axis=0)
#     all_dist_to_mean = {}
#     for i in range(len(all_file_mean)):
#         all_dist_to_mean[all_file_name[i]] = distance(all_file_mean[i], all_mean)
#
#     sorted_distance_to_mean = sorted(all_dist_to_mean.items(), key=lambda kv: kv[1], reverse=True)
#     print("sorted_distance_to_mean:")
#     print(sorted_distance_to_mean[0:recall])
#     preview_image_batch(root, [root + image_file_name[0] for image_file_name in sorted_distance_to_mean[0:recall]],
#                         "avg_distance.html")
#
#     sibling_distance = {}
#     for index in file_to_mean.keys():
#         d = 0
#         num = 0
#         if index - 1 in file_to_mean:
#             d += distance(file_to_mean[index], file_to_mean[index - 1])
#             num += 1
#         if index + 1 in file_to_mean:
#             d += distance(file_to_mean[index], file_to_mean[index + 1])
#             num += 1
#         sibling_distance_value = d / num
#         sibling_distance[index] = sibling_distance_value
#
#     sorted_sibling_distance_to_mean = sorted(sibling_distance.items(), key=lambda kv: kv[1], reverse=True)
#     print("sorted_sibling_distance_to_mean:")
#     print(sorted_sibling_distance_to_mean[0:recall])
#
#     preview_image_batch(root, [root + index_to_filename(image_file_name[0]) for image_file_name in
#                                sorted_sibling_distance_to_mean[0:recall]],
#                         "sibling_distance.html")
