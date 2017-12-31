import tkinter as tk
from random import randint

from devide_and_conqer import DivideAndConquer
from graham import Graham
from point import Point
from quick_hull import QuickHull
from simple_polygon import SimplePolygon

from convex_hull.approximation import Approximation


class MainWindow(tk.Tk):
    # radius of drawn points on canvas
    vertex_radius = 3

    # flag to lock the canvas when drawn
    LOCK_FLAG = False
    width = 800
    height = 600

    def __init__(self):
        super(self.__class__, self).__init__()

        self.title("Convex Hull")

        self.frame = tk.Frame(self, borderwidth=1)
        self.frame.pack(fill=tk.BOTH, expand=1)

        self.canvas = tk.Canvas(self.frame, width=self.width, height=self.height)
        self.canvas.config(background='white')
        self.canvas.bind('<Button-1>', self.create_point_on_click)
        self.canvas.pack()

        self.button_frame = tk.Frame(self)
        self.button_frame.pack()

        self.random_button = tk.Button(self.button_frame, text='Random', width=25, command=self.random_on_click)
        self.random_button.pack(side=tk.RIGHT)

        self.clear_button = tk.Button(self.button_frame, text='Clear', width=25, command=self.clear_on_click)
        self.clear_button.pack(side=tk.RIGHT)

        self.calculate_button = tk.Button(self.button_frame, text='Quick Hull', width=25,
                                          command=lambda: self.on_click(QuickHull))
        self.calculate_button.pack(side=tk.RIGHT)

        self.graham_button = tk.Button(self.button_frame, text='Graham', width=25,
                                       command=lambda: self.on_click(Graham))
        self.graham_button.pack(side=tk.RIGHT)

        self.polygon_button = tk.Button(self.button_frame, text='Polygon', width=25,
                                       command=lambda: self.on_click_polygon())
        self.polygon_button.pack(side=tk.RIGHT)

        self.dc_button = tk.Button(self.button_frame, text='Divide and conquer', width=25,
                                        command=lambda: self.on_click(DivideAndConquer))
        self.dc_button.pack(side=tk.RIGHT)

        self.dc_button = tk.Button(self.button_frame, text='Approximation', width=25,
                                   command=lambda: self.on_click(Approximation))
        self.dc_button.pack(side=tk.RIGHT)

    def on_click(self, Algorithm):
        if not self.LOCK_FLAG:
            self.LOCK_FLAG = True

            canvas_objects = self.canvas.find_all()
            points = []
            for p in canvas_objects:
                coord = self.canvas.coords(p)
                points.append(Point(coord[0] + self.vertex_radius, coord[1] + self.vertex_radius))
            qh = Algorithm(self.canvas, True)
            qh.start(points)


    def on_click_polygon(self):
        canvas_objects = self.canvas.find_all()
        points = []
        for p in canvas_objects:
            coord = self.canvas.coords(p)
            points.append(Point(coord[0] + self.vertex_radius, coord[1] + self.vertex_radius))
        qh = SimplePolygon(self.canvas, True)
        qh.init(points)

        margin = 50
        numb = randint(100, 300)
        for i in range(numb):
            x, y = randint(0 + margin, self.width - margin), randint(0 + margin, self.height - margin)
            res = qh.localize(Point(x, y))
            if res:
                col = "green"
            else:
                col = "red"
            self.canvas.create_oval(x - self.vertex_radius, y - self.vertex_radius,
                                    x + self.vertex_radius,
                                    y + self.vertex_radius, fill=col)

            self.canvas.update()

    def random_on_click(self):
        margin = 50
        self.clear_on_click()
        numb = randint(50, 150)
        for i in range(numb):
            x, y = randint(0 + margin, self.width - margin), randint(0 + margin, self.height - margin)
            self.canvas.create_oval(x - self.vertex_radius, y - self.vertex_radius,
                                    x + self.vertex_radius, y + self.vertex_radius,
                                    fill="black")

    def clear_on_click(self):
        self.LOCK_FLAG = False
        self.canvas.delete(tk.ALL)

    def create_point_on_click(self, event):
        if not self.LOCK_FLAG:
            self.canvas.create_oval(event.x - self.vertex_radius, event.y - self.vertex_radius,
                                    event.x + self.vertex_radius,
                                    event.y + self.vertex_radius, fill="black")


if __name__ == '__main__':
    app = MainWindow()
    app.mainloop()
