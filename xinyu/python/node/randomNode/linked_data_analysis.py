# Created by Xinyu Zhu on 2021/7/15, 2:32
from common.io.file.PlainTextClient import read_file_from
from collections import defaultdict


def start_with_number(line: str):
    return line.strip()[0].isdigit()


def get_company_name(line: str):
    striped_line = line.strip()
    return striped_line[striped_line.index(" "):].strip()


def get_count(line: str):
    striped_line = line.strip()
    return int(striped_line[0:striped_line.index(" ")])


if __name__ == '__main__':
    filename = "C:/my C/Finance/linkedInData.txt"
    data = read_file_from(filename)

    all_company_num = defaultdict(int)
    is_first_line_of_unitivery = False
    for line in data.split("\n"):
        if line != "":
            # print(line)
            if not start_with_number(line):
                is_first_line_of_unitivery = True
                continue
            else:
                if is_first_line_of_unitivery:
                    is_first_line_of_unitivery = False
                    continue

            company_name = get_company_name(line)
            count = get_count(line)
            all_company_num[company_name] += count

    total = sum(all_company_num.values())
    percentage = defaultdict(float)
    for key in all_company_num:
        percentage[key] = all_company_num[key] / total

    sorted_result = sorted(all_company_num.items(), key=lambda kv: kv[1], reverse=True)
    for item in sorted_result:
        print(item[0] + "\t" + str(item[1]))
