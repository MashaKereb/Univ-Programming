import time
import tkinter as tk

from algorithm import Algorithm
from point import Point

from convex_hull.cyclic_list import CyclicList


class GrahamPoint(Point):
    center = None

    def __init__(self, x, y, center=None):
        super().__init__(x, y)
        if self.center is None:
            GrahamPoint.center = center

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __le__(self, other):
        return self.__lt__(other) or self.__eq__(other)

    def __lt__(self, other):
        x1, y1 = self.x - self.center.x, self.y - self.center.y
        x2, y2 = other.x - self.center.x, other.y - self.center.y

        if y1 >= 0 > y2:
            return False
        if y2 >= 0 > y1:
            return True
        if y1 == 0 and y2 == 0:
            if x1 >= 0 > x2:
                return True
            if x2 >= 0 > x1:
                return False
            else:
                return self.distance_to(GrahamPoint.center) < other.distance_to(GrahamPoint.center)
        angle = x1 * y2 - x2 * y1
        if angle == 0:
            return self.distance_to(GrahamPoint.center) < other.distance_to(GrahamPoint.center)
        return angle < 0


class Graham(Algorithm):
    def __init__(self, canvas=None, animate=False):
        super().__init__(canvas, animate)
        self.all_points = []
        self.hull_points = CyclicList()

    def start(self, points):
        center = Point.find_center(points)
        GrahamPoint.center = center

        for point in points:
            self.all_points.append(GrahamPoint(point.x, point.y))

        sorted_points = sorted(self.all_points)
        self.hull_points = CyclicList(sorted_points)
        return self.graham_scan(self.hull_points)

    def graham_scan(self, points):
        if len(points) <= 3:
            return points

        if not isinstance(points, CyclicList):
            points = CyclicList(points)

        start = points.find_node(max(points, key=lambda x: x.y))
        v = start
        f = False

        while not f:
            if v.next == start:
                f = True
            if not Point.check_is_turn_ccw(v.data, v.next.data, v.next.next.data):
                v = v.next
            else:
                points.remove_node(v.next)
                if v != start:
                    v = v.prev
            if self.animate:
                self.draw()
                time.sleep(0.05)

        return list(points)

    def draw(self):
        self.canvas.delete(tk.ALL)
        for point in self.all_points:
            point.draw(self.canvas, color="red")

        for point in self.hull_points.iter_nodes():
            point.data.draw(self.canvas, color="green")
            self.canvas.create_line(point.data.x, point.data.y, point.next.data.x, point.next.data.y)
        GrahamPoint.center.draw(self.canvas, color="blue")
        self.canvas.update()
