# Created by Xinyu Zhu on 2021/8/10, 0:36
import math


# 利用中点法解target_function=0的近似解, 在(start_x 到 end_x)的区间寻找, 当误差小于allowed_error时返回
# 有时间再优化
def mid_point_method(target_function, start_x, end_x, allowed_error):
    if end_x - start_x >= allowed_error:
        mid_x = (end_x + start_x) / 2
        start_y = target_function(start_x)
        mid_y = target_function(mid_x)
        if mid_y == 0:
            return mid_x, 0
        else:
            if mid_y * start_y < 0:
                return mid_point_method(target_function, start_x, mid_x, allowed_error)
            else:
                return mid_point_method(target_function, mid_x, end_x, allowed_error)
    else:
        return (end_x + start_x) / 2


def daily_return_func(rate):
    p399 = math.pow(rate, 399)
    return (p399 - 1) / (rate - 1) * (57000 - 22468.5) / 400 + p399 * 22652.92 - 67381.57


def avg_growth_func(xi, rate, add):
    return xi * rate + add


if __name__ == '__main__':
    rate = mid_point_method(daily_return_func, 1.0001, 2, 0.00001)
    print(rate)
    yearly_rate = rate ** 365 - 1
    print(yearly_rate)
    yearly_rate += 0.01

    # yearly_rate = 0.1

    daily_rate = math.pow(yearly_rate + 1, 1/365)
    print(daily_rate)

    test_year = 35
    base1 = 67381.57
    for i in range(365 * test_year):
        base1 = avg_growth_func(base1, daily_rate, 19500 / 365)
    print(base1)

    base2 = 67381.57
    for i in range(365 * test_year):
        base2 = avg_growth_func(base2, rate, 19500 / 365)
    print(base2)

    print(base1 - base2)
    print((base1 - base2) / test_year / 365)
