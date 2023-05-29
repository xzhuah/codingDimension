# Created by Xinyu Zhu on 2/6/2023, 1:34 AM
import requests


def download_image(url, filename):
    response = requests.get(url)

    with open(filename, 'wb') as f:
        f.write(response.content)


def random_image(filename):
    download_image("https://source.unsplash.com/random", filename)


if __name__ == '__main__':
    download_image("https://source.unsplash.com/random", "random.jpg")
