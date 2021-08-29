import json
from common.io.file.PlainTextClient import read_file, read_io_file


def read_json(filename):
    data = read_file(filename)
    json_data = json.loads(data)
    return json_data


def read_io_json(filename):
    data = read_io_file(filename)
    json_data = json.loads(data)
    return json_data
