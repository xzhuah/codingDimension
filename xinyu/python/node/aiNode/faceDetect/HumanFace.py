# Created by Xinyu Zhu on 1/5/2023, 1:32 AM
# import dlib
# print(dlib.DLIB_USE_CUDA)
# print(dlib.cuda.get_num_devices())

# import face_recognition
import cv2
from node.imageStreamNode.imageStreamProcessor import WebCameraImageStreamProcessor, ScreenImageStreamProcessor
from anime_face_detector import create_detector


# import dlib
# print(dlib.DLIB_USE_CUDA)
# print(dlib.cuda.get_num_devices())

def add_box(image, top, left, bottom, right, color=(255, 0, 255), thickness=2):
    return cv2.rectangle(image, (int(top), int(left)), (int(bottom), int(right)),
                         color, thickness)  # 绘制矩形框


def add_bbox(image, face_location, color=(255, 0, 255), thickness=2):
    top, right, bottom, left = face_location
    return add_box(image, left, top, right, bottom, color, thickness)


# def detect_face(image):
#     if isinstance(image, str):
#         image = face_recognition.load_image_file(image)
#     return face_recognition.face_locations(image)

def detect_anime_face(image, detector):
    return detector(image)


# class FaceDetectForCamera(WebCameraImageStreamProcessor):
#
#     def process(self, image):
#         face_locations = face_recognition.face_locations(image)
#         for face_location in face_locations:
#             image = add_bbox(image, face_location)
#         cv2.imshow('frame', image)
#         cv2.waitKey(1)


class AnimeFaceDetectForCamera(WebCameraImageStreamProcessor):
    def __init__(self):
        super(AnimeFaceDetectForCamera, self).__init__()
        self.detector = create_detector("yolov3")

    def process(self, image):
        face_locations = detect_anime_face(image, self.detector)
        max_score = -1
        for face_location in face_locations:
            image = add_box(image, face_location["bbox"][0], face_location["bbox"][1], face_location["bbox"][2],
                            face_location["bbox"][3])
            max_score = max(face_location["bbox"][4], max_score)
        print(max_score)
        # resize
        scale_percent = 400
        image = cv2.resize(image,
                           (int(image.shape[1] * scale_percent / 100), int(image.shape[0] * scale_percent / 100)),
                           interpolation=cv2.INTER_AREA)
        cv2.imshow('frame', image)
        cv2.waitKey(1)
        return max_score


#
# class FaceDetectForScreen(ScreenImageStreamProcessor):
#
#     def process(self, image):
#         face_locations = face_recognition.face_locations(image)
#         for face_location in face_locations:
#             image = add_bbox(image, face_location)
#         cv2.imshow('frame', image)
#         cv2.waitKey(1)


class AnimeFaceDetectForScreen(ScreenImageStreamProcessor):
    def __init__(self):
        super(AnimeFaceDetectForScreen, self).__init__()
        self.detector = create_detector("yolov3")

    def process(self, image):
        face_locations = detect_anime_face(image, self.detector)
        max_score = -1
        for face_location in face_locations:
            image = add_box(image, face_location["bbox"][0], face_location["bbox"][1], face_location["bbox"][2],
                            face_location["bbox"][3])
            max_score = max(face_location["bbox"][4], max_score)
        print(max_score)
        cv2.imshow('frame', image)
        cv2.waitKey(1)
        return max_score


if __name__ == '__main__':
    face = AnimeFaceDetectForScreen()
    face.start()

    # image = face_recognition.load_image_file("D:/Work/3DWorkspace/MMDResource/human/精选/啊.jpg")
    # face_locations = detect_face(image)
    #
    # for face_location in face_locations:
    #     # Print the location of each face in this image
    #     image = add_bbox(image, face_location)
    # while (True):
    #     cv2.imshow('frame', image)
    #
    #     cv2.waitKey(1)
