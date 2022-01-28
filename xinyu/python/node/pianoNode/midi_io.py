# Created by Xinyu Zhu on 2022/1/27, 21:37
from midiutil.MidiFile import MIDIFile
import pygame

pygame.init()


class MidiIO:
    def __init__(self, pygame):
        self.pygame = pygame

    def load_play_midi(self, filename, suggested_music_ms=1000):
        self.pygame.mixer.music.load(filename)
        self.pygame.mixer.music.play()

        while pygame.mixer.music.get_busy():
            self.pygame.time.wait(suggested_music_ms)


# create your MIDI object
mf = MIDIFile(1)  # only 1 track
track = 0  # the only track

time = 0  # start at the beginning
mf.addTrackName(track, time, "Sample Track")
mf.addTempo(track, time, 120)

# add some notes
channel = 0
volume = 100

pitch = 60  # C4 (middle C)
time = 0  # start on beat 0
duration = 1  # 1 beat long
mf.addNote(track, channel, pitch, time, duration, volume)

pitch = 64  # E4
time = 2  # start on beat 2
duration = 1  # 1 beat long
mf.addNote(track, channel, pitch, time, duration, volume)

pitch = 67  # G4
time = 4  # start on beat 4
duration = 1  # 1 beat long
mf.addNote(track, channel, pitch, time, duration, volume)

# write it to disk
with open("output.mid", 'wb') as outf:
    mf.writeFile(outf)
