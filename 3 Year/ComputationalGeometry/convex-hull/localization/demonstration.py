import tkinter as tk
from random import randint
from localizator import ChainLocalizator, StripLocalizator

from convex_hull.point import Point
from generator import PlanarGenerator


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
        self.chain = False

        self.start = False

        self.title("Point localization")

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
        def switch_f():
            self.chain = not self.chain

        self.switch_button = tk.Button(self.button_frame, text='Switch', width=25, command=switch_f)
        self.switch_button.pack(side=tk.RIGHT)

        self.graph = None
        self.generator = PlanarGenerator()
        self.res = []
        self.localizator = None

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

            # self.reg_tree = RegionTreeSearch(self.canvas, True)
            # self.reg_tree.start(points)
            # self.canvas.bind('<B1-Motion>', self.region_tree_on_click)

    def random_on_click(self):
        self.clear_on_click()
        self.graph = self.generator.generate()
        self.generator.draw(self.canvas)
        self.localizator = StripLocalizator(self.graph, canvas=self.canvas)
        self.chain_localizator = ChainLocalizator(self.graph, canvas=self.canvas)



    def clear_on_click(self):
        self.LOCK_FLAG = False
        self.canvas.bind('<Button-1>', self.create_point_on_click)
        self.canvas.delete(tk.ALL)

    def create_point_on_click(self, event):
        if not self.LOCK_FLAG:
            for elem in self.res:
                self.canvas.delete(elem)

        point = Point(event.x, event.y)
        res = []
        if self.chain:
            chains = self.chain_localizator.get_region(point)
            res.extend(self.chain_localizator.draw_chain(chains[0], self.canvas))
            res.extend(self.chain_localizator.draw_chain(chains[1], self.canvas))
        else:
            reg = self.localizator.get_region(point)
            res.extend(self.localizator.draw(reg))
        res.append(self.canvas.create_oval(event.x - self.vertex_radius,
                                       event.y - self.vertex_radius,
                                       event.x + self.vertex_radius,
                                       event.y + self.vertex_radius, fill="green"))
        self.res = res


if __name__ == '__main__':
    app = MainWindow()
    app.mainloop()
