# Created by Xinyu Zhu on 2021/4/20, 17:58
import pysynth as ps
import numpy as np
import re

# 先限定音符12356 中国风五声调式 这样听起来比较自然
notes = np.array(["c4", "d4", "e4", "g4", "a4", ])
# 音符时值
durations = np.array([1, 2, 4, -2, -4, -8])
# 随机生成音符 重音穿插其中
sn = []
for t in range(16):
    n = np.random.randint(0, len(notes))
    note = notes[n] + "*"
    sn.append(note)
for i in range(np.random.randint(3, 5)):
    note0 = notes[np.random.randint(0, len(notes))]
    sn.append(note0)
# 随机生成音符时值序列 形成长短参差变幻的节奏
dn = []
for i in range(len(sn)):
    duration = durations[np.random.randint(0, len(durations))]
    nn = sn[i]
    dn.append(duration)
# 将音符和时值合并成旋律
melody = tuple(zip(sn, dn))
print(melody)
# 将乐谱合成到声音文件
ps.make_wav(melody, fn=r"right.wav")
print("ok")
