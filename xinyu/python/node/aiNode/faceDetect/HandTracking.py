# Created by Xinyu Zhu on 1/7/2023, 8:11 PM
import cv2
import mediapipe as mp
import time
from node.imageStreamNode.imageStreamProcessor import WebCameraImageStreamProcessor, ScreenImageStreamProcessor, \
    BaseImageStreamProcessor


class ScreenImageHandPoseDetector(ScreenImageStreamProcessor):
    def __init__(self):
        super(ScreenImageHandPoseDetector, self).__init__()
        self.hands_detector = mp.solutions.hands.Hands(static_image_mode=True)
        self.mpDraw = mp.solutions.drawing_utils

    def process(self, image):
        results = self.hands_detector.process(image)
        if not results.multi_hand_landmarks:
            print("no hands")
        if results.multi_hand_landmarks:
            for handlms in results.multi_hand_landmarks:
                print(handlms)
                for id, lm in enumerate(handlms.landmark):
                    # print(id, lm)
                    h, w, c = image.shape
                    cx, cy = int(lm.x * w), int(lm.y * h)
                    # print(id, cx, cy)
                    # if id == 5:
                    cv2.circle(image, (cx, cy), 15, (139, 0, 0), cv2.FILLED)

                self.mpDraw.draw_landmarks(image, handlms, mp.solutions.hands.HAND_CONNECTIONS)
        # cv2.putText(img, str(int(fps)), (10, 70), cv2.FONT_HERSHEY_SIMPLEX, 3, (139, 0, 0), 3)
        if image.shape[1] < 800:
            # resize
            scale_percent = 275
            image = cv2.resize(image,
                               (int(image.shape[1] * scale_percent / 100), int(image.shape[0] * scale_percent / 100)),
                               interpolation=cv2.INTER_AREA)

        cv2.imshow("Image", image)
        cv2.waitKey(1)


class WebCameraHandPoseDetector(WebCameraImageStreamProcessor):
    def __init__(self):
        super(WebCameraHandPoseDetector, self).__init__()
        self.hands_detector = mp.solutions.hands.Hands(model_complexity=1)  # 0 - 1, 1: highest accuracy, slowest
        self.mpDraw = mp.solutions.drawing_utils

    def process(self, image):
        image.flags.writeable = False
        results = self.hands_detector.process(image)
        image.flags.writeable = True
        if results.multi_hand_landmarks:
            for handlms in results.multi_hand_landmarks:
                for id, lm in enumerate(handlms.landmark):
                    # print(id, lm)
                    h, w, c = image.shape
                    cx, cy = int(lm.x * w), int(lm.y * h)
                    # print(id, cx, cy)
                    # if id == 5:
                    cv2.circle(image, (cx, cy), 15, (139, 0, 0), cv2.FILLED)

                self.mpDraw.draw_landmarks(image, handlms, mp.solutions.hands.HAND_CONNECTIONS)
        # cv2.putText(img, str(int(fps)), (10, 70), cv2.FONT_HERSHEY_SIMPLEX, 3, (139, 0, 0), 3)
        if image.shape[1] < 800:
            # resize
            scale_percent = 275
            image = cv2.resize(image,
                               (int(image.shape[1] * scale_percent / 100), int(image.shape[0] * scale_percent / 100)),
                               interpolation=cv2.INTER_AREA)

        cv2.imshow("Image", image)
        cv2.waitKey(1)


if __name__ == '__main__':
    # ScreenImageStreamProcessor() WebCameraImageStreamProcessor()
    hand_pose = WebCameraHandPoseDetector()
    hand_pose.start()
