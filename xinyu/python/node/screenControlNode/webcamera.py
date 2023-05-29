# Created by Xinyu Zhu on 1/4/2023, 9:43 PM

# import the opencv library
import cv2
from anime_face_detector import create_detector
from node.aiNode.faceDetect.AnimeFace import add_facebox
import pyautogui
# import pygetwindow
import numpy as np

# define a video capture object
vid = cv2.VideoCapture(0)
detector = create_detector("yolov3")

while (True):

    # Capture the video frame
    # by frame
    # ret, frame = vid.read()
    frame = pyautogui.screenshot()
    frame = cv2.cvtColor(np.array(frame), cv2.COLOR_RGB2BGR)

    frame, score = add_facebox(frame, detector)
    # Display the resulting frame
    cv2.imshow('frame', frame)
    # print(score)

    # the 'q' button is set as the
    # quitting button you may use any
    # desired button of your choice
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# After the loop release the cap object
vid.release()
# Destroy all the windows
cv2.destroyAllWindows()
