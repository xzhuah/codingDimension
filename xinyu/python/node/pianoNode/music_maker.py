# Created by Xinyu Zhu on 2020/12/21, 1:31

import pygame.midi
import time
import threading
from common.io.file.PlainTextClient import read_file
from common.io.file import project_root


class Player:
    def __init__(self):

        # 对于简谱上的一个数, 实际的声调应该在基础音阶的基础上增加多少
        self.unit_offset = {
            "1": 0,
            "2": 2,
            "3": 4,
            "4": 5,
            "5": 7,
            "6": 9,
            "7": 11
        }

        # 播放器所使用的初始基础音阶, 60为midi对于C大调的1(哆)
        self.base_freq = 60

        # 根据简谱的调性, 播放器所使用的的基础音阶应该在初始基础音阶的基础上增加多少
        self.key_offset = {
            "C": 0,
            "Db": 1,
            "C#": 1,
            "D": 2,
            "Eb": 3,
            "E": 4,
            "F": 5,
            "Gb": 6,
            "F#": 6,
            "G": 7,
            "Ab": 8,
            "A": 9,
            "Bb": 10,
            "B": 11,
            "Cb": 11
        }

        # 每拍时间长度(s)
        self.pt = 1.0

        # 乐器种类: 普通钢琴
        self.instrument = 0

        # 用于发声的midi模块初始化
        pygame.midi.init()
        self.output = pygame.midi.Output(pygame.midi.get_default_output_id())
        self.output.set_instrument(self.instrument)

    def single_note_to_num(self, single_note: str):
        '''
            将简谱中的单个音阶装换为实际频率代码
            支持的规则: 1: 普通的音阶, 1#: 升半调, .1: 升7度, 1..: 降两个7度, 1..#或者1#..: 降两个7度后升半调
            .1.: 会被解析为升两个7度, .1##.: 会忽略重复的#, 被解析为..1#
            不支持: .8.: 不在1~7的范围内, 返回-1, 不会播放, @1: 含有不合法字符, 返回-1 不会播放
        '''

        # 以.开头视为需要升若干个7度, 否则需要降
        should_increase_7 = (single_note[0] == ".")

        # 具体升降多少个7度看.的个数
        cycle_should_change = single_note.count(".")

        # 去除.和#之后的字符视为要播放的音阶
        unit_key = single_note.replace(".", "").replace("#", "")

        if unit_key in self.unit_offset:
            unit_key_freq = self.base_freq + self.unit_offset[unit_key]
        else:
            return -1

        if "#" in single_note:
            unit_key_freq += 1

        if should_increase_7:
            unit_key_freq += cycle_should_change * 12
        else:
            unit_key_freq -= cycle_should_change * 12
        return unit_key_freq

    def connected_note_to_num_list(self, connected_note: str):
        '''
            将类似 1_2. 这种有联系的多个音符转成一个数组, 每个数组中全部音阶播放的完的时间会相同, 也就是一个拍的时间
            多个拍组成一个音轨的一个小节(segment), 4/4拍的音乐中, 4拍为一节

            多个音轨组成的一个小节, 我们叫做一节(section), 也就是我们编写音乐中的一行, 播放时一节一节地解析播放

            单个音节也能形成一个长度为1的数组
        '''
        result = []
        for single_unit in connected_note.split("_"):
            result.append(self.single_note_to_num(single_unit))
        return result

    def segment_digitialize(self, segment: str):
        '''
            "1 1 1 3" is a segment
            "4.._1. 6.._1. 4.._1. 6.._1." is another segment
        '''
        result = []
        for connected_note in segment.split(" "):
            result.append(self.connected_note_to_num_list(connected_note))
        return result

    def section_digitialize(self, section: str):
        '''
        "1 1 1 3|6.._3. 1._3. 6.._3. 1._3." is a section with 2 segments in two different channels
        if the first 1 should be played by 0.5 seconds, then the 6.. should be played by 0.25 seconds and the 3.
        should be played by 0.25 seconds, 6.. and 1 will be played together, then 3.

        [
        [[1],      [1],     [1],      [3]],
        [[6.., 3.],[1., 3.],[6.., 3.],[1., 3.]]
        ]
        '''
        result = []
        for segment in section.split("|"):
            segment = segment.strip()
            result.append(self.segment_digitialize(segment))
        return result

    def play_section(self, section: list):
        '''
        Play a digitialized section,
        '''
        # 每个channel的节拍数量应该相同, 可以用0补齐
        channel_length = len(section[0])
        for i in range(channel_length):
            threads = []
            for segment in section:
                threads.append(threading.Thread(target=self.play_connected_node, args=(segment[i], 127)))

            [thr.start() for thr in threads]
            [thr.join() for thr in threads]

    def play_connected_node(self, connected_node: list, velocity=127):
        time_interval = self.pt / len(connected_node)
        for node in connected_node:
            if node != -1:
                self.output.note_on(node, velocity)
            time.sleep(time_interval)

    def play_music(self, attr, music):
        self.set_attr(attr)
        for section in music:
            if len(section) > 0:
                self.play_section(self.section_digitialize(section))

    def set_attr(self, attr: str):
        attrs = attr.split(",")
        key = attrs[0].replace("1=", "")
        self.base_freq = 60 + self.key_offset[key]
        self.pt = 60 / int(attrs[2].replace("pm=", ""))

        if "offset=" in attr:
            self.base_freq += int(attrs[3].replace("offset=", "")) * 12

        if "ins=" in attr:
            self.instrument = int(attrs[4].replace("ins=", ""))
            self.output.set_instrument(self.instrument)

    def read_and_play_music(self, filename):
        content = read_file(filename)
        content = content.split("\n")
        self.play_music(content[0], content[1:])

    def close(self):
        self.output.close()


if __name__ == '__main__':
    player = Player()

    player.read_and_play_music(project_root + "resources/myHeartWillGoOn.ply")

    player.close()
