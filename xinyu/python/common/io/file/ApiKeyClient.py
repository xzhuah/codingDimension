from common.io.file.JsonFileClient import read_json

all_keys = read_json("apikeys.pass")


def get_key(api_name):
    if api_name in all_keys:
        return all_keys[api_name]
    else:
        return ""


def get_openai_key():
    if "openai" in all_keys:
        return all_keys["openai"]
    else:
        return ""


if __name__ == '__main__':
    print(get_openai_key())
