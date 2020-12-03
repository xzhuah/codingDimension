from common.io.file.JsonFileClient import read_json
from common.io.file import project_root

all_keys = read_json(project_root + "resources/apikeys.pass")


def get_key(api_name):
    if api_name in all_keys:
        return all_keys[api_name]
    else:
        return ""
