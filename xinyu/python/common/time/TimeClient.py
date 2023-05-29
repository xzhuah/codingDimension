# Created by Xinyu Zhu on 2020/12/2, 22:31
import datetime
import time

DEFAULT_DATETIME_FORMAT = "%Y-%m-%d %H:%M:%S"
DEFAULT_DATE_FORMAT = "%Y-%m-%d"
NUMERICAL_DATE_FORMAT = "%Y%m%d"


def str_to_timestamp_in_millisecond(time_str, format=DEFAULT_DATETIME_FORMAT):
    d = datetime.datetime.strptime(time_str, format)
    t = d.timetuple()
    return int(time.mktime(t)) * 1000


def timestamp_in_millisecond_to_str(timestamp, format=DEFAULT_DATETIME_FORMAT):
    time_local = time.localtime(timestamp // 1000)
    return time.strftime(format, time_local)


def get_current_timestamp_in_millisecond():
    return time.time() * 1000

def quick_now():
    return timestamp_in_millisecond_to_str(get_current_timestamp_in_millisecond())

if __name__ == '__main__':
    timestamp = str_to_timestamp_in_millisecond("2020-12-2 22:34:50")
    timestr = timestamp_in_millisecond_to_str(get_current_timestamp_in_millisecond(), NUMERICAL_DATE_FORMAT)
    print(timestr)
