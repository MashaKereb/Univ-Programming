import tkinter as tk
from random import randint
from primitives import Point
from delaunay import Delaunay


class MainWindow(tk.Tk):
    # radius of drawn points on canvas
    vertex_radius = 3

    # flag to lock the canvas when drawn
    LOCK_FLAG = False
    width = 800
    height = 600

    def __init__(self):
        super(self.__class__, self).__init__()

        self.title("Delaunay")

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

        self.calculate_button = tk.Button(self.button_frame, text='Delaunay triangulation', width=25,
                                          command=lambda: self.on_click(Delaunay))
        self.calculate_button.pack(side=tk.RIGHT)

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
