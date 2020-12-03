# Provide basic functions to read string content from file


def read_file(filename):
    with open(filename, encoding='utf-8') as f:
        data = f.read()
    return data