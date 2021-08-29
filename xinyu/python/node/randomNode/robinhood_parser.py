# Created by Xinyu Zhu on 2021/7/30, 0:41

from common.io.file.PlainTextClient import read_file


def read_as_table(string, title):
    content = string.split("\n")
    result = []
    for j, line in enumerate(content):
        if len(line) == 0:
            break
        index = j % len(title)
        if index == 0:
            result.append({})
        result[-1][title[index]] = line.replace("$", "").replace(",", "")
        if str(result[-1][title[index]]).isdecimal():
            result[-1][title[index]] = float(result[-1][title[index]])

    return result


if __name__ == '__main__':
    content = read_file("buffer.txt")

    result = read_as_table(content, ["Name", "Symbol", "Shares", "Price", "Average Cost", "Total Return", "Equity"])
    for obj in result:
        for value in obj.values():
            print(value, "\t", end="")
        print()
