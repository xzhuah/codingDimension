# Created by Xinyu Zhu on 2021/6/6, 21:08

from turtle import Turtle
import matplotlib.pyplot as plt
import turtle

def draw_array(x: list):
    plt.title("Line graph")
    plt.scatter(range(len(x)), x, color="red", marker='o')
    plt.show()


def draw_rectangle(turtle: Turtle, llx, lly, width, height):
    turtle.up()
    turtle.goto(llx, lly)
    turtle.begin_fill()
    turtle.down()
    turtle.goto(llx + width, lly)
    turtle.goto(llx + width, lly + height)
    turtle.goto(llx, lly + height)
    turtle.end_fill()


if __name__ == '__main__':
    # tur = Turtle()
    # wn = turtle.Screen()
    # wn.title("Turtle Demo")
    # wn.setworldcoordinates(0, 0, 500, 500)
    # tur.speed(0)
    # draw_rectangle(tur, 0, 0, 500, 500)

    draw_array([1112, 100002, 2202, 2939, 39992])

    

