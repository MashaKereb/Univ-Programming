import time
import tkinter as tk
from queue import deque
from random import choice

from algorithm import Algorithm

from convex_hull.point import Point


class QuickHull(Algorithm):
    EPS = 0.000001
    colors = ['green', 'black', 'brown', 'blue', 'orange', 'grey']

    def __init__(self, canvas=None, animate=False):
        super().__init__(canvas, animate)
        self.hull_points = []
        self.hull_lines = []

        self.help_lines = deque(maxlen=5)
        self.all_points = None

    def start(self, points):
        self.all_points = points

        leftmost_point = min(points, key=lambda x: x.x)
        self.hull_points.append(leftmost_point)

        self.hull_points = self.quick_hull(leftmost_point, Point(leftmost_point.x, leftmost_point.y - self.EPS), points)
        self.hull_points.insert(0, leftmost_point)
        self.help_lines = []

        self.draw()

    def quick_hull(self, l, r, points):
        if len(points) == 0:
            self.hull_lines.append((l, r))
            return points

        m = max(points, key=lambda x: (x.distance_to_line(l, r), -x.x))
        self.hull_points.append(m)

        if self.animate:
            col = choice(self.colors)
            self.draw()
            self.help_lines.append((l, m, col))
            self.help_lines.append((r, m, col))
            time.sleep(1)

        left = list(filter(lambda x: Point.check_is_turn_ccw(l, m, x), points))
        right = list(filter(lambda x: Point.check_is_turn_ccw(r, m, x) is False, points))
        l_points = self.quick_hull(l, m, left)
        r_points = self.quick_hull(m, r, right)
        l_points.append(m)
        l_points.extend(r_points)
        return l_points

    def draw(self):
        self.canvas.delete(tk.ALL)
        for line in self.hull_lines:
            self.draw_line(*line, color="red")

        for point in self.all_points:
            point.draw(self.canvas, color="black")

        for point in self.hull_points:
            point.draw(self.canvas, color="red")

        for x, y, col in self.help_lines:
            self.draw_line(x, y, color=col)

        self.canvas.update()

    def draw_line(self, p1, p2, *, color="green"):
        self.canvas.create_line(p1.x, p1.y, p2.x, p2.y, fill=color)
