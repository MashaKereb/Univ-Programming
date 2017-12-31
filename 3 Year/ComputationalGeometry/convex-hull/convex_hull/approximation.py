import tkinter as tk
from collections import defaultdict

from algorithm import Algorithm
from cyclic_list import CyclicList

from convex_hull.graham import Graham


class Approximation(Algorithm):
    K = 5

    def __init__(self, canvas=None, animate=False):

        super().__init__(canvas, animate)

        self.leftmost = None
        self.rightmost = None

        self.points = []
        self.lines = []

        self.hull_points = []
        self.max_points = []

    def start(self, points):
        self.points = points

        self.leftmost = min(points, key=lambda point: point.x)
        self.rightmost = max(points, key=lambda point: point.x)
        a, b = int(self.leftmost.x), int(self.rightmost.x) + 1

        step = (b - a) // self.K
        self.lines = [x for x in range(a, b + step, step)]

        return self.get_hull()

    def get_hull(self):
        groups = defaultdict(list)

        for p in self.points:
            for l in self.lines:
                if p.x < l:
                    groups[l].append(p)
                    break

        points = []
        for k in groups.keys():
            if len(groups[k]) == 0:
                continue
            points.append(max(groups[k], key=lambda point: point.y))
            points.append(min(groups[k], key=lambda point: point.y))

        self.max_points = points

        if self.leftmost not in self.max_points:
            self.max_points.append(self.leftmost)
        if self.rightmost not in self.max_points:
            self.max_points.append(self.rightmost)

        gh = Graham()

        self.hull_points = gh.start(self.max_points)

        self.draw()
        return self.hull_points

    def draw(self):
        self.canvas.delete(tk.ALL)
        for point in self.points:
            point.draw(self.canvas, color="black")
        for line in self.lines:
            self.canvas.create_line(line, 1000, line, 0, fill="blue")
        for point in self.max_points:
            point.draw(self.canvas, color="red")
        self.draw_hull(self.hull_points)
        self.canvas.update()

    def draw_hull(self, points, color="green", width=1):
        if not isinstance(points, CyclicList):
            points = CyclicList(points)

        for point in points.iter_nodes():
            self.canvas.create_line(point.data.x, point.data.y, point.next.data.x, point.next.data.y, fill=color,
                                    width=width)
