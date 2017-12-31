import tkinter as tk

from voronoi import Voronoi
from random import randint
from point import  Point


class MainWindow:
    # radius of drawn points on canvas
    vertex_radius = 2

    # flag to lock the canvas when drawn
    LOCK_FLAG = False
    width = 500
    height = 500

    def __init__(self, master):
        self.master = master
        self.master.title("Voronoi diagram")

        self.frame = tk.Frame(self.master, borderwidth=1)
        self.frame.pack(fill=tk.BOTH, expand=1)

        self.canvas = tk.Canvas(self.frame, width=self.width, height=self.height)
        self.canvas.config(background='white')
        self.canvas.bind('<Button-1>', self.create_point_on_click)
        self.canvas.pack()

        self.button_frame = tk.Frame(self.master)
        self.button_frame.pack()

        self.random_button = tk.Button(self.button_frame, text='Random', width=25, command=self.random_on_click)
        self.random_button.pack(side=tk.LEFT)

        self.calculate_button = tk.Button(self.button_frame, text='Calculate', width=25, command=self.calculate_on_click)
        self.calculate_button.pack(side=tk.LEFT)

        self.clear_button = tk.Button(self.button_frame, text='Clear', width=25, command=self.clear_on_click)
        self.clear_button.pack(side=tk.LEFT)

    def calculate_on_click(self):
        if not self.LOCK_FLAG:
            self.LOCK_FLAG = True

            canvas_objects = self.canvas.find_all()
            points = []
            for p in canvas_objects:
                coord = self.canvas.coords(p)
                points.append(Point(coord[0] + self.vertex_radius, coord[1] + self.vertex_radius))

            vp = Voronoi(points)
            Voronoi.width = self.width
            Voronoi.height = self.height
            vp.start(canvas=self.canvas, animate=True)
            # vp.process()
            # lines = vp.get_output()
            # self.draw_lines(lines)

    def random_on_click(self):
        self.clear_on_click()
        numb = randint(50, 100)
        for i in range(numb):
            x, y = randint(0 + 5, self.width - 5), randint(0 + 5, self.height - 5)
            self.canvas.create_oval(x - self.vertex_radius, y - self.vertex_radius,
                                    x + self.vertex_radius, y + self.vertex_radius,
                                    fill="black")

    def clear_on_click(self):
        self.LOCK_FLAG = False
        self.canvas.delete(tk.ALL)

    def create_point_on_click(self, event):
        if not self.LOCK_FLAG:
            self.canvas.create_oval(event.x - self.vertex_radius, event.y - self.vertex_radius, event.x + self.vertex_radius,
                                    event.y + self.vertex_radius, fill="black")

    def draw_lines(self, lines):
        for l in lines:
            self.canvas.create_line(l[0], l[1], l[2], l[3], fill='blue')


if __name__ == '__main__':
    root = tk.Tk()
    app = MainWindow(root)
    root.mainloop()
