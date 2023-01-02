# Created by Xinyu Zhu on 11/11/2022, 12:33 AM
import json


class MotionData:

    def __init__(self, bone_name="", frame=0, px=0, py=0, pz=0, rx=0, ry=0, rz=0, rw=0):
        self.bone_name = bone_name
        self.frame = frame

        # bone position
        self.px = px
        self.py = py
        self.pz = pz

        # bone rotation
        self.rx = rx
        self.ry = ry
        self.rz = rz
        self.rw = rw

    def to_json(self):
        return json.dumps(self.to_map())

    def to_map(self):
        return {"bone": self.bone_name,
                           "frame": self.frame,
                           "px": self.px,
                           "py": self.py,
                           "pz": self.pz,
                           "rx": self.rx,
                           "ry": self.ry,
                           "rz": self.rz,
                           "rw": self.rw,
                           }


if __name__ == '__main__':
    m = MotionData()
    print(m.to_json())
