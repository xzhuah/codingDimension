# Created by Xinyu Zhu on 11/18/2022, 7:16 PM
from PIL import Image
import cv2
import numpy as np


def simple_diff(image1, image2):
    image1 = Image.open(image1)
    image2 = Image.open(image2)
    buffer1 = np.asarray(image1)
    buffer2 = np.asarray(image2)

    buffer3 = buffer1 - buffer2
    differenceImage = Image.fromarray(buffer3)
    differenceImage.show()


# MSE between two images
def mse_gray(img1, img2):
    h, w = img1.shape
    diff = cv2.subtract(img1, img2)
    err = np.sum(diff ** 2)
    mse = err / (float(h * w))
    return mse, diff

def mse_rgb(img1, img2):
    r, g, b = img1.shape
    diff = cv2.subtract(img1, img2)
    err = np.sum(diff ** 2)
    mse = err / (float(r * g * 3))
    return mse, diff



def mse_diff_gray(image1, image2):
    # load the input images
    img1 = cv2.imread(image1)
    img2 = cv2.imread(image2)

    # convert the images to grayscale
    img1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)

    error, diff = mse_gray(img1, img2)
    print("Image matching Error between the two images:", error)

    cv2.imshow("difference", diff)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

def mse_diff_rgb(image1, image2):
    # load the input images
    img1 = cv2.imread(image1)
    img2 = cv2.imread(image2)

    error, diff = mse_rgb(img1, img2)
    print("Image matching Error between the two images:", error)

    cv2.imshow("difference", diff)
    cv2.waitKey(0)
    cv2.destroyAllWindows()


if __name__ == '__main__':
    image1Path = "C:/myC/Personal/3DWorkSpace/Learning/renderingSetting/img/1.png"
    image2Path = "C:/myC/Personal/3DWorkSpace/Learning/renderingSetting/img/2.png"
    mse_diff_rgb(image1Path, image2Path)
