# Created by Xinyu Zhu on 2021/8/29, 3:40
import os

# Can customize
DEFAULT_PATH_SEPARATOR = "/"

# Don't Customize
ILLEGAL_CHARS_FOR_FOLDER_NAME = {DEFAULT_PATH_SEPARATOR, "<", ">", "[", "]", "(", ")", os.linesep}
ILLEGAL_CHARS_FOR_FILENAME = {DEFAULT_PATH_SEPARATOR, "<", ">", "[", "]", "(", ")", os.linesep}
ILLEGAL_CHARS_FOR_URI = {"(", ")", os.linesep}
ILLEGAL_CHARS_FOR_TAG = {DEFAULT_PATH_SEPARATOR, "<", ">", "{", "}", ",", "[", "]", "(", ")", os.linesep}
