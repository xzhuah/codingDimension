# Created by Xinyu Zhu on 2022/6/26, 3:13

for i in range(30,-1,-1):
    for j in range(10):
        print("({j},{i})".format(i=i, j=j), end=" ")
    print()