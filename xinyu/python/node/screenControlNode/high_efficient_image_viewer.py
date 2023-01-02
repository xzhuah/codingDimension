# Created by Xinyu Zhu on 12/21/2022, 10:23 PM
import PIL.Image as Image
from pynput.keyboard import Key, Listener
import threading
import pygetwindow
import matplotlib.pyplot as plt
#
# try:
#     from Tkinter import *
#     import tkFileDialog as filedialog
# except ImportError:
#     from tkinter import *
#     from tkinter import filedialog
# import PIL.ImageTk

import time

resolution = 0.5
image_per_view = 1
buffer = 10
image_folder = "C:/myC/Personal/3DWorkSpace/Project/primaryStar_Ying/images/"
image_index_start = 31


def to_filename(index, folder=image_folder):
    result = str(index)
    return folder + (4 - len(result)) * "0" + result + ".png"


def  start_view_image():
    global im
    t = threading.Thread(target=maintain_piano)
    t.start()
    im = Image.open(to_filename(image_index_start))
    plt.imshow(im)
    plt.show()




def maintain_piano():
    with Listener(
            on_press=on_press,
            on_release=on_release) as listener:
        listener.join()


def on_press(key):
    global im, image_index_start
    plt.clf()  # will make the plot window empty
    im.close()
    image_index_start += 1
    now = time.perf_counter()
    im = Image.open(to_filename(image_index_start))
    print("open", time.perf_counter() - now)
    now = time.perf_counter()
    # print(to_filename(image_index_start))
    plt.imshow(im)
    print("imshow", time.perf_counter() - now)
    now = time.perf_counter()
    plt.show()
    print("show", time.perf_counter() - now)
    # frame.la.config(image=image, bg="#000000", width=image.width(), height=image.height())
    # frame.pack()


def on_release(key):
    pass


# class App(Frame):
#     def chg_image(self):
#         if self.im.mode == "1":  # bitmap image
#             self.img = PIL.ImageTk.BitmapImage(self.im, foreground="white")
#         else:  # photo image
#             self.img = PIL.ImageTk.PhotoImage(self.im)
#         self.la.config(image=self.img, bg="#000000",
#                        width=self.img.width(), height=self.img.height())
#
#     def open(self):
#         filename = filedialog.askopenfilename()
#         if filename != "":
#             self.im = PIL.Image.open(filename)
#         self.chg_image()
#         self.num_page = 0
#         self.num_page_tv.set(str(self.num_page + 1))
#
#     def seek_prev(self):
#         self.num_page = self.num_page - 1
#         if self.num_page < 0:
#             self.num_page = 0
#         self.im.seek(self.num_page)
#         self.chg_image()
#         self.num_page_tv.set(str(self.num_page + 1))
#
#     def seek_next(self):
#         self.num_page = self.num_page + 1
#         try:
#             self.im.seek(self.num_page)
#         except:
#             self.num_page = self.num_page - 1
#         self.chg_image()
#         self.num_page_tv.set(str(self.num_page + 1))
#
#     def __init__(self, master=None):
#         Frame.__init__(self, master)
#         self.master.title('Image Viewer')
#
#         self.num_page = 0
#         self.num_page_tv = StringVar()
#
#         fram = Frame(self)
#         Button(fram, text="Open File", command=self.open).pack(side=LEFT)
#         Button(fram, text="Prev", command=self.seek_prev).pack(side=LEFT)
#         Button(fram, text="Next", command=self.seek_next).pack(side=LEFT)
#         Label(fram, textvariable=self.num_page_tv).pack(side=LEFT)
#         fram.pack(side=TOP, fill=BOTH)
#
#         self.la = Label(self)
#         self.la.pack()
#
#         self.pack()


if __name__ == "__main__":
    # app = App()
    # app.mainloop()
    start_view_image()
