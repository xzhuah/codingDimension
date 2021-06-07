# Created by Xinyu Zhu on 2021/6/3, 21:44

class MusicDataManager:
    def __init__(self):

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
        self.all_key = ["1", "1#", "2", "2#", "3", "4", "4#", "5", "5#", "6", "6#", "7"]

        self.music_data = []
        self.init()

    def init(self):
        self.current_time = 0
        # support at most 12 channels
        self.last_real_chords = [0] * 12
        self.end_time_to_chord = {}

    def visualize_single_chord(self, single_chord):
        if single_chord["note"] != 0:
            base_diff = single_chord["note"] - single_chord["base"]
            offset_num = abs(base_diff // 12)
            remaining = base_diff % 12
            text = self.all_key[remaining]
            if base_diff < 0:
                text = text + "." * offset_num
            else:
                text = "." * offset_num + text
        else:
            text = 0
        print(text, single_chord["start"], single_chord["end"], single_chord["ins"])

    def output_current(self):
        self.music_data = []
        for key in self.end_time_to_chord:
            for chord in self.end_time_to_chord[key]:
                self.music_data.append(chord)
        self.music_data.sort(key=lambda k: k["start"])
        # for data in self.music_data:
        #     self.visualize_single_chord(data)

    def parse_music(self, music_section: list, pt: float, base_freq: int):
        for notes in music_section:
            # These notes are played at the same time
            max_note_length = 1
            for note in notes:
                if len(note["note"]) > max_note_length:
                    max_note_length = len(note["note"])
            for j, note in enumerate(notes):
                notes_start_time = self.current_time
                for i, chord in enumerate(note["note"]):
                    isDuration = False
                    if chord == -1:
                        isDuration = True
                        chord = self.last_real_chords[j]
                    single_chord = {
                        "ins": note["ins"],
                        "note": chord,
                        "velocity": note["velocity"],
                        "base": base_freq,
                        "start": notes_start_time + i * pt / len(note["note"]) * 1.0,
                        "end": notes_start_time + (i + 1) * pt / len(note["note"]) * 1.0,
                    }
                    self.last_real_chords[j] = chord
                    if not isDuration:
                        if single_chord["end"] not in self.end_time_to_chord:
                            self.end_time_to_chord[single_chord["end"]] = []
                        self.end_time_to_chord[single_chord["end"]].append(single_chord)
                    else:
                        if single_chord["start"] in self.end_time_to_chord:
                            for last_node in self.end_time_to_chord[single_chord["start"]]:
                                if last_node["note"] == single_chord["note"]:
                                    self.end_time_to_chord[single_chord["start"]].remove(last_node)
                                    last_node["end"] = single_chord["end"]
                                    if last_node["end"] not in self.end_time_to_chord:
                                        self.end_time_to_chord[last_node["end"]] = []
                                    self.end_time_to_chord[last_node["end"]].append(last_node)
                        else:
                            self.end_time_to_chord[single_chord["start"]] = [single_chord]
            self.current_time += pt


# if __name__ == '__main__':
#     player = MidiPlayer()
#     musicDataManager = MusicDataManager()
#     player.pt, player.base_freq = 1, 62
#     # line = ".2 .2 0 7	|	2_2_.2_.1 4_.1_.2_.1 3 2	|	5 6 5 4	|	1 5. 7. 2"
#     # musicDataManager.parse_music(player.parse_section(line), player.pt, player.base_freq)
#     # line = ".2 .2 0 7	|	2_2_.2_.1 4_.1_.2_.1 3 2	|	5 6 5 4	|	0 5. 7. 2"
#     line = "1. 2#. 3. 4. 5#. 6. 7. 1# 2 3# 4# 5 6 7# .1# .2 .3 .4 .5 .6 .7#"
#     musicDataManager.parse_music(player.parse_section(line), player.pt, player.base_freq)
#     musicDataManager.output_current()


