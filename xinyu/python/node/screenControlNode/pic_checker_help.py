# Created by Xinyu Zhu on 10/4/2022, 7:40 PM
from pynput.keyboard import Key, Listener
import threading
import pygetwindow



def maintain_piano():
    with Listener(
            on_press=on_press,
            on_release=on_release) as listener:
        listener.join()

def on_press(key):
   pass


def _pre_pre_process_key(key):
    key = str(key).lower()
    key = key.strip()
    if key == "'''":
        return "'"
    else:
        key = key.strip("'")
        if len(key) == 1:
            return key
        else:
            return key

pic_select = set()
def on_release(key):
    folder_name = "images"
    key = _pre_pre_process_key(key)
    if key == 'z':
        for t in pygetwindow.getAllTitles():
            if "FastStone Image Viewer 7.7" in t:
                print(t[0:4])
                frame = int(t[0:4])
                print("add", frame)
                pic_select.add(frame)
    elif key == 'b':
        for t in pygetwindow.getAllTitles():
            if "FastStone Image Viewer 7.7" in t:
                frame = int(t[0:4])
                if frame in pic_select:
                    print("remove", frame)
                    pic_select.remove(frame)
    elif key == "k":
        a = list(pic_select)
        a.sort()
        print(a)

    # folder_name = "images"
    # key = _pre_pre_process_key(key)
    # if key == 'a':
    #     for t in pygetwindow.getAllTitles():
    #         print(t)
    #         if t.startswith(folder_name) and ".png" in t:
    #             print(t[10:14])
    #             frame = int(t[10:14])
    #             print("add", frame)
    #             pic_select.add(frame)
    # elif key == 'd':
    #     for t in pygetwindow.getAllTitles():
    #         if t.startswith(folder_name) and ".png" in t:
    #             frame = int(t[10:14])
    #             if frame in pic_select:
    #                 print("remove", frame)
    #                 pic_select.remove(frame)
    # elif key == "l":
    #     a = list(pic_select)
    #     a.sort()
    #     print(a)



if __name__ == '__main__':

    t = threading.Thread(target=maintain_piano)
    t.start()