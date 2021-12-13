# Created by Xinyu Zhu on 2021/11/22, 21:46

# Given an array of length n, return the element that appears more than n//2 times in the array. return -1 if no such element.
# O(n) O(1)

def find_major_number(arr: list) -> int:
    if len(arr) == 0:
        return -1
    curr = arr[0]
    curr_freq = 1
    for i in range(1, len(arr)):
        if curr_freq == 0:
            curr = arr[i]
            curr_freq = 1
            continue
        if arr[i] == curr:
            curr_freq += 1
            # optional acc
            if curr_freq > len(arr) // 2:
                return curr
        else:
            curr_freq -= 1
    if curr_freq == 0:
        return -1
    else:
        count_curr = 0
        for ele in arr:
            if ele == curr:
                count_curr += 1
        if count_curr > len(arr) // 2:
            return curr
        else:
            return -1


if __name__ == '__main__':
    arr = [1, 4, 1, 2, 3, 4, 1, 1, 4, 4, 4, 4, 4]
    print(len(arr), find_major_number(arr))
