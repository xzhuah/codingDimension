# Provide basic functions to read string content from file
from common.io.file import project_input_root, project_output_root, project_io_root

def read_file_from(filename) -> str:
    with open(filename, encoding='utf-8') as f:
        data = f.read()
    return data


def get_file(filename, mode="r"):
    mode = mode.lower()
    if mode == "r":
        return open(project_input_root + filename, mode=mode, encoding="utf-8")
    elif mode == "w":
        return open(project_output_root + filename, mode=mode, encoding="utf-8")
    else:
        raise Exception(mode + " is not a valid mode")


def get_io_file(filename, mode="r"):
    return open(project_io_root + filename, mode=mode, encoding="utf-8")


def read_file(filename) -> str:
    with open(project_input_root + filename, encoding='utf-8') as f:
        data = f.read()
    return data


def read_io_file(filename) -> str:
    with open(project_io_root + filename, encoding='utf-8') as f:
        data = f.read()
    return data
