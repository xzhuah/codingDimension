# Created by Xinyu Zhu on 1/27/2023, 11:07 PM
from amzqr import amzqr
import os

def to_color_qr_code(encode_text: str, background_image: str, output:str):
    # v: 1~40 for length
    # picture: combine with a picture, colorized
    # level: L、M、Q、H，从左到右依次升高

    dir_name = os.path.dirname(output)
    if dir_name == "":
        dir_name = "./"
    dir_name = os.path.abspath(dir_name)
    print(dir_name)
    return amzqr.run(encode_text, version=2, level='H', picture=background_image, colorized=True, contrast=1.0, brightness=1.0,
                     save_name=os.path.basename(output), save_dir=dir_name
                     )


def to_qr_code(encode_text: str, output:str):
    # v: 1~40 for length
    # picture: combine with a picture, colorized
    # level: L、M、Q、H，从左到右依次升高

    dir_name = os.path.dirname(output)
    if dir_name == "":
        dir_name = "./"
    dir_name = os.path.abspath(dir_name)
    print(dir_name)
    return amzqr.run(encode_text, version=2, level='H', picture=None, colorized=False, contrast=1.0, brightness=1.0,
                     save_name=os.path.basename(output), save_dir=dir_name
                     )

from googlesearch import search
if __name__ == '__main__':
    # to_qr_code("https://github.com/x-hw/amazing-qr/blob/master/README-cn.md", "test.png")
    for url in search("python"):
        print(url)