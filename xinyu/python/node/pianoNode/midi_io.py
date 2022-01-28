# Created by Xinyu Zhu on 2022/1/27, 21:37
from midiutil.MidiFile import MIDIFile
import pygame
from node.pianoNode.midi_player import MidiPlayer
from node.pianoNode.ply_standardlizer import auto_format_for_file
from common.io.file.PlainTextClient import read_io_file
from common.io.file import project_io_root


# 为了支持midi_io， ply文件中不能出现以-开头的一段音符，要把-合并到前一行去
# 现有的ply文件无法支持固定音轨编号，只能按第一段为音轨0，第二段为音轨1排布
# beat_per_minute如果音乐中途需要改变的话，支持起来比较复杂(需要提前预留一定数量的音轨，造成浪费)，推荐分成两段两个midi文件处理


class MidiIO:
    def __init__(self, beat_per_minute, track_num=8):
        self.midi_player = MidiPlayer()
        self.mf = MIDIFile(track_num)
        start_time = 0
        for track in range(track_num):
            self.mf.addTrackName(track, start_time, "Track" + str(track))
            self.mf.addTempo(track, start_time, beat_per_minute)

        # by default use only 1 channel, and fix volume
        self.channel = 0
        self.volume = 127

    # track: 第几个音轨 from 0
    # pitch: 音调 Integer
    # start_beat: 开始的拍数 can be float
    # duration_beat： 持续的拍数 can be float
    def addNote(self, track, pitch, start_beat, duration_beat, volume=-1, channel=-1):
        if pitch <= 0:
            return
        if volume == -1:
            volume = self.volume
        if channel == -1:
            channel = self.channel
        self.mf.addNote(track, channel, pitch, start_beat, duration_beat, volume)

    # only use the first line to speculate bpm
    def parse_ply(self, filename):
        auto_format_for_file(filename)
        data = read_io_file(filename)

        content_in_line = data.split("\n")
        if "pm=" not in content_in_line[0]:
            print("please provide pm=beat_per_minute in the first line of ply file")
            return
        else:
            attrs = content_in_line[0].split(",")
            for attr_unit in attrs:
                if "pm=" in attr_unit:
                    beat_per_min = int(attr_unit.replace("pm=", ""))
                    # print("got beat_per_min:", beat_per_min)

        track_num_required = 1
        for line in content_in_line[1:]:
            track_num = line.count("|") + 1
            if track_num > track_num_required:
                track_num_required = track_num
        # print("need track_num", track_num_required)

        self.mf = MIDIFile(track_num_required)
        start_time = 0
        for track in range(track_num):
            self.mf.addTrackName(track, start_time, "Track" + str(track))
            self.mf.addTempo(track, start_time, beat_per_min)

        track_start_beats = 0
        next_start = 0
        for line in self.midi_player.parse_music_line(content_in_line):
            # print(line)
            # print(self.line_to_tracks(line))
            for i, track in enumerate(self.line_to_tracks(line)):
                next_start = self.track_to_beats(track, track_start_beats, i)
            track_start_beats = next_start

    def convert_ply_to_midi(self, filename):
        self.parse_ply(filename)
        self.save_midi(filename + ".mid")


    def line_to_tracks(self, line):
        beat_length = len(line)
        track_num = len(line[0])
        result = []
        for i in range(track_num):
            track = []
            for beat in range(beat_length):
                track.append(line[beat][i]["note"])
            result.append(track)
        return result

    def track_to_beats(self, track, track_start_beats=0, track_index=0):
        current_pitch = track[0][0]
        current_pitch_start = track_start_beats
        if current_pitch < 0:
            # print("midi io does not support track start with 0, please combine it with formal track line")
            return
        init = True
        current_beat_length = 1 / len(track[0])
        for i in range(len(track)):
            for j in range(len(track[i])):
                if init:
                    init = False
                    continue
                if track[i][j] == -1:
                    current_beat_length += 1 / len(track[i])
                else:
                    # add current_pitch (if not zero) with current_beat_length to the track with addNote
                    # print(track_index, current_pitch, current_pitch_start, current_beat_length)
                    self.addNote(track_index, current_pitch, current_pitch_start, current_beat_length)

                    # then
                    current_pitch = track[i][j]
                    current_pitch_start += current_beat_length
                    current_beat_length = 1 / len(track[i])
        # add current_pitch (if not zero) with current_beat_length to the track with addNote
        # print(track_index, current_pitch, current_pitch_start, current_beat_length)
        self.addNote(track_index, current_pitch, current_pitch_start, current_beat_length)
        # for next track start
        return track_start_beats + len(track)


    def save_midi(self, filename: str):
        if not filename.endswith(".mid"):
            filename += ".mid"
        # write it to disk
        with open(project_io_root + filename, 'wb') as outf:
            self.mf.writeFile(outf)


def load_and_play_midi(self_pygame, filename, suggested_music_ms=1000):
    self_pygame.mixer.music.load(project_io_root + filename)
    self_pygame.mixer.music.play()

    while self_pygame.mixer.music.get_busy():
        self_pygame.time.wait(suggested_music_ms)


if __name__ == '__main__':
    file_name = "level5.ply"

    convert = False
    # convert = True

    if convert:
        midiIO = MidiIO(120, 2)
        midiIO.convert_ply_to_midi(file_name)
    else:
        pygame.init()
        load_and_play_midi(pygame, file_name+".mid")

#
# # create your MIDI object
# mf = MIDIFile(1)  # only 1 track
# track = 0  # the only track
#
# time = 0  # start at the beginning
# mf.addTrackName(track, time, "Sample Track")
# mf.addTempo(track, time, 120)
#
# # add some notes
# channel = 0
# volume = 100
#
# pitch = 60  # C4 (middle C)
# time = 0  # start on beat 0
# duration = 1  # 1 beat long
# mf.addNote(track, channel, pitch, time, duration, volume)
#
# pitch = 64  # E4
# time = 2  # start on beat 2
# duration = 1  # 1 beat long
# mf.addNote(track, channel, pitch, time, duration, volume)
#
# pitch = 67  # G4
# time = 4  # start on beat 4
# duration = 1  # 1 beat long
# mf.addNote(track, channel, pitch, time, duration, volume)

# write it to disk
# with open("output.mid", 'wb') as outf:
#     mf.writeFile(outf)
