import json
from common.io.file.PlainTextClient import read_file


def read_json(filename):
    data = read_file(filename)
    json_data = json.loads(data)
    return json_data
