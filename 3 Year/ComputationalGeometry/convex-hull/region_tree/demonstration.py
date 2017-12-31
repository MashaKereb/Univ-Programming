import tkinter as tk
from random import randint

from convex_hull.point import Point
from region_tree import RegionTreeSearch


class MainWindow(tk.Tk):
    # radius of drawn points on canvas
    vertex_radius = 3

    # flag to lock the canvas when drawn
    LOCK_FLAG = False
    width = 800
    height = 600

    def __init__(self):
        super(self.__class__, self).__init__()
        self.point = None
        self.point2_index = None
        self.rectangle = None
        self.prev_res = None

        self.start = False

        self.title("Region search")

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

        self.calculate_button = tk.Button(self.button_frame, text='RegionTree', width=25,
                                          command=self.on_click)
        self.calculate_button.pack(side=tk.RIGHT)

        self.reg_tree = None


    def region_tree_on_click(self, event):
        if self.point is None:
            self.point = Point(event.x, event.y)
            self.canvas.create_oval(event.x - self.vertex_radius,
                                    event.y - self.vertex_radius,
                                    event.x + self.vertex_radius,
                                    event.y + self.vertex_radius, fill="red")
            self.canvas.update()
        else:
            point = Point(event.x, event.y)
            if self.prev_res:
                for i in self.prev_res:
                    i.draw(self.canvas, color="black")
            if self.rectangle:
                for i in self.rectangle:
                    self.canvas.delete(i)

            self.canvas.delete(self.point2_index)
            self.point2_index = self.canvas.create_oval(event.x - self.vertex_radius,
                                                        event.y - self.vertex_radius,
                                                        event.x + self.vertex_radius,
                                                        event.y + self.vertex_radius, fill="red")

            self.rectangle = self.canvas.create_line(point.x, point.y, point.x, self.point.y, fill="blue"), \
                             self.canvas.create_line(point.x, point.y, self.point.x, point.y, fill="blue"), \
                             self.canvas.create_line(self.point.x, point.y, self.point.x, self.point.y, fill="blue"), \
                             self.canvas.create_line(point.x, self.point.y, self.point.x, self.point.y, fill="blue")
            res = self.reg_tree.get_points(point, self.point)
            self.prev_res = res
            self.reg_tree.draw(res)
            self.canvas.update()


    def on_click(self):
        if not self.LOCK_FLAG:
            self.point = None
            self.point2_index = None
            self.rectangle = None
            self.prev_res = None

            canvas_objects = self.canvas.find_all()
            points = []
            for p in canvas_objects:
                coord = self.canvas.coords(p)
                points.append(Point(coord[0] + self.vertex_radius, coord[1] + self.vertex_radius))

            self.reg_tree = RegionTreeSearch(self.canvas, True)
            self.reg_tree.start(points)
            self.canvas.bind('<B1-Motion>', self.region_tree_on_click)

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
        self.canvas.bind('<Button-1>', self.create_point_on_click)
        self.canvas.delete(tk.ALL)

    def create_point_on_click(self, event):
        if not self.LOCK_FLAG:
            self.canvas.create_oval(event.x - self.vertex_radius,
                                    event.y - self.vertex_radius,
                                    event.x + self.vertex_radius,
                                    event.y + self.vertex_radius, fill="black")


if __name__ == '__main__':
    app = MainWindow()
    app.mainloop()
