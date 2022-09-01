# Created by Xinyu Zhu on 2022/5/14, 22:36
from common.io.file.FileManagerClient import search_files
from urllib import parse


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
        image_components.append(image_template2("." + to_relative_path(root, pic_file)))
    image_content = "\n".join(image_components)
    index = 2
    html = html_template2(image_content, image_width=80 * index, image_height=160 * index)
    with open(ensure_postfix(root) + output_filename, mode="w", encoding="utf-8") as f:
        f.write(html)
    print(len(image_paths))


if __name__ == '__main__':

    # 要处理的文件夹目录
    root = ensure_path_format("C:/myC/Personal/3DWorkSpace/Project/ningguang/image")

    all_pic_file = search_files(root, {".png", ".jpeg", ".jpg"})
    # all_pic_file = search_files(root, {".png"})
    image_components = []
    n = 5
    batch_size = 150

    start = batch_size * (n)
    end = start + batch_size
    if end > len(all_pic_file):
        end = len(all_pic_file)
    for pic_file in all_pic_file[start:end]:
        image_components.append(image_template2("." + to_relative_path(root, pic_file)))

    image_content = "\n".join(image_components)
    index = 2
    html = html_template2(image_content, image_width=80 * index, image_height=160 * index)

    with open(ensure_postfix(root) + "preview.html", mode="w", encoding="utf-8") as f:
        f.write(html)
    print(end)
