# Created by Xinyu Zhu on 1/4/2023, 10:10 PM

# pip install face_recognition
import cv2
import signal
import pyautogui
from anime_face_detector import create_detector
from node.aiNode.faceDetect.AnimeFace import add_facebox
import numpy as np
from common.annotations.annotation import simple_thread


def timeout_handler(signum, frame):
    raise TimeoutError("Timed out!")


class BaseImageStreamProcessor:
    # mode: 0: start and until stop is called, ignore any exception
    # mode: 1: stop if process throw error or next_image return None
    # timeout_per_frame: 当每帧处理时间超过这个数值时，直接返回None, 必须大于0才起作用, 单位是秒
    def __init__(self, mode=0, timeout_per_frame=0):
        self.mode = mode
        self.stop_signal = False
        self.timeout = timeout_per_frame

    # the main process function
    def process(self, image):
        return image

    def process_timeout(self, image):
        if self.timeout <= 0:
            return self.process(image)
        else:
            signal.signal(signal.SIGALRM, timeout_handler)
            signal.alarm(self.timeout)
            try:
                # Run your code here
                # This will only be executed if the timeout is not triggered
                return self.process(image)
            except TimeoutError:
                # This will be executed if the timeout is triggered
                return None
            finally:
                # Reset the alarm
                signal.alarm(0)

    # generator
    def next_image(self):
        return None

    @simple_thread
    def start(self):
        self.stop_signal = False
        image = None
        while not self.stop_signal:
            image = self.next_image()
            if image is None:
                if self.mode == 1:
                    return
                else:
                    continue
            try:
                self.process_timeout(image)
            except Exception as E:
                print("Exception:", E)
                if self.mode == 1:
                    return
                else:
                    continue

    def stop(self):
        # gracefully stop, wait until process finish
        self.stop_signal = True


class ScreenImageStreamProcessor(BaseImageStreamProcessor):
    def __init__(self):
        super(ScreenImageStreamProcessor, self).__init__()

    def next_image(self):
        screenshot = pyautogui.screenshot()
        cv2_image = cv2.cvtColor(np.array(screenshot), cv2.COLOR_RGB2BGR)
        return cv2_image

    def process(self, image):
        cv2.imshow('frame', image)
        cv2.waitKey(1)

    def stop(self):
        super(ScreenImageStreamProcessor, self).stop()
        cv2.destroyAllWindows()


class WebCameraImageStreamProcessor(BaseImageStreamProcessor):

    def __init__(self):
        super(WebCameraImageStreamProcessor, self).__init__()
        self.vid = cv2.VideoCapture(0)

    def next_image(self):
        success, frame = self.vid.read()
        return frame

    def process(self, image):
        cv2.imshow('frame', image)
        cv2.waitKey(1)

    def stop(self):
        super(WebCameraImageStreamProcessor, self).stop()
        # After the loop release the cap object
        self.vid.release()
        # Destroy all the windows
        cv2.destroyAllWindows()


class ScreenCharacterDetector(ScreenImageStreamProcessor):
    def __init__(self):
        super(ScreenCharacterDetector, self).__init__()
        self.detector = create_detector("yolov3")

    def process(self, image):
        frame, score = add_facebox(image, self.detector)
        # Display the resulting frame
        cv2.imshow('frame', frame)
        cv2.waitKey(1)


class CameraCharacterDetector(WebCameraImageStreamProcessor):
    def __init__(self):
        super(CameraCharacterDetector, self).__init__()
        self.detector = create_detector("yolov3")

    def process(self, image):
        frame, score = add_facebox(image, self.detector)
        # Display the resulting frame
        cv2.imshow('frame', frame)
        cv2.waitKey(1)


if __name__ == '__main__':
    a = CameraCharacterDetector()  # ScreenImageStreamProcessor()
    a.start()
    # time.sleep(10)
    # a.stop()
