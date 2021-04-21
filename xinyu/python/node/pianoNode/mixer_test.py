# Created by Xinyu Zhu on 2021/4/20, 18:17
import pygame

pygame.mixer.init()
# pygame.mixer.set_num_channels(2)

# sound0 = pygame.mixer.Sound('left.wav')
# sound0.set_volume(0.2)

m = pygame.mixer.Sound('left.wav')
n = pygame.mixer.Sound('right.wav')
# m.set_volume(0.2)
# n.set_volume(0.2)

channel1 = pygame.mixer.Channel(1)
channel2 = pygame.mixer.Channel(2)


while True:

    channel1.play(m)
    channel1.set_volume(0.5, 0.0)
    channel2.play(n)
    channel2.set_volume(0.7, 0.0)

# # channel0 = pygame.mixer.find_channel()
#
# # Play the sound (that will reset the volume to the default).
# channel0.play(sound0)
# # Now change the volume of the specific speakers.
# # The first argument is the volume of the left speaker and
# # the second argument is the volume of the right speaker.
# channel0.set_volume(1.0, 0.0)

pygame.mixer.quit()
