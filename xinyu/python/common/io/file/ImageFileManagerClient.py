# Created by Xinyu Zhu on 9/30/2022, 9:06 PM

from PIL import Image
from common.io.file.FileManagerClient import get_all_filepath, to_filename
import threading

# def batch_image_processor(all_images_path: list, worker_num: int, processor):
#     pass


def batch_image_processor_parallel(filenames, processor, batch_size=10):
    batch_num = len(filenames) // batch_size
    result = []
    for batch in range(batch_num):
        result.append(filenames[batch * batch_size: (batch + 1) * batch_size])

    if len(filenames) % batch_size != 0:
        result.append(filenames[batch_num * batch_size:])

    output_buffer = {}
    all_tasks = [threading.Thread(target=batch_image_processor_sequence, args=(filebatch, processor, output_buffer)) for filebatch in
                 result]
    [task.start() for task in all_tasks]
    [task.join() for task in all_tasks]
    return output_buffer

def batch_image_processor_sequence(filenames, processor, output_buffer):
    for filename in filenames:
        output_buffer[filename] = processor(filename)


def image_4k_to_2k_processor(image_path):
    out_root = "C:/Users/Xinyu Zhu/Pictures/2K高清壁纸/"
    image_file = Image.open(image_path)
    if image_file.width > 2560 or image_file.height > 1440:
        image_file.resize((2560, 1440))
    image_file.save(out_root + to_filename(image_path))
    return image_file.width > 2560 or image_file.height > 1440

if __name__ == '__main__':
    root = "C:/Users/Xinyu Zhu/Pictures/4K高清壁纸/"

    image_4k_to_2k_processor(root + "yayay.png")

    # all_images = get_all_filepath(root)
    # print(all_images)
    #
    # batch_image_processor_parallel(all_images, image_4k_to_2k_processor, 10)



