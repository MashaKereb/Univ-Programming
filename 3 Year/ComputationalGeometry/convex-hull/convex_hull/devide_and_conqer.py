import time
import tkinter as tk

from algorithm import Algorithm
from cyclic_list import CyclicList
from point import Point
from simple_polygon import SimplePolygon

from convex_hull.graham import Graham, GrahamPoint


class DivideAndConquer(Algorithm):
    K = 10

    def __init__(self, canvas=None, animate=False):
        super().__init__(canvas, animate)
        self.hull_points = []
        self.all_points = []

    def start(self, points):
        self.all_points = points
        self.hull_points = self.divide_and_conquer(CyclicList(points))
        if self.animate:
            self.draw(color="red")
        return self.hull_points

    def draw_hull(self, points, color="green", width=1):
        if not isinstance(points, CyclicList):
            points = CyclicList(points)

        for point in points.iter_nodes():
            self.canvas.create_line(point.data.x, point.data.y, point.next.data.x, point.next.data.y, fill=color, width=width)
        self.canvas.update()

    def draw(self, color="blue"):
        self.canvas.delete(tk.ALL)
        for point in self.all_points:
            point.draw(self.canvas)

        if not isinstance(self.hull_points, CyclicList):
             self.hull_points = CyclicList(self.hull_points)
        self.draw_hull(self.hull_points, color=color)


    def divide_and_conquer(self, points):
        if not  isinstance(points, CyclicList):
            points = CyclicList(points)

        if len(points) <= self.K:
            gh = Graham()
            res = gh.start(points)
            if self.animate:
                self.draw()
                self.draw_hull(points, color="orange")
                self.draw_hull(res, color="red")
                time.sleep(1)
            return CyclicList(res)
        else:
            first, second = points.split()
            points_1 = self.divide_and_conquer(first)
            points_2 = self.divide_and_conquer(second)

            res = self.merge(points_1, points_2)

            if self.animate:
                self.draw()
                self.draw_hull(points_1, color="blue")
                self.draw_hull(points_2, color="green")
                self.draw_hull(res, color="red", width=2)
                time.sleep(2)
            return res

    def merge(self, points1, points2):
        p = Point.find_center(points1)
        p2 = SimplePolygon()
        p2.init(points2)
        if p2.localize(p):
            new_list = self.merge_lists( points1, points2, center=p)

            gh = Graham()
            return CyclicList(gh.graham_scan(new_list))
        else:
            c_list = CyclicList(points2)
            anchors = self.all_anchor_points(p, c_list)

            for i in anchors:
                i.data.draw(self.canvas, color="orange", radius=2)
            p.draw(self.canvas, color="blue")
            self.canvas.update()
            assert len(anchors) == 2

            if Point.check_is_turn_ccw(anchors[0].data, p, anchors[1].data):
                right, left = anchors[0], anchors[1]
            else:
                right, left = anchors[1], anchors[0]
            item = left.next
            while item != right:
                item = item.next
                c_list.remove_node(item.prev)

            new_list = self.merge_lists(points1, c_list, center=p)
            gh = Graham()
            return CyclicList(gh.graham_scan(new_list))

    @staticmethod
    def merge_lists(l1, l2, center=None):
        prev_center = GrahamPoint.center
        if center is not None:
            GrahamPoint.center=center
        l1.normalize()
        l2.normalize()
        l1,l2 = list(l1), list(l2)
        res = []
        i, j = 0, 0
        while i < len(l1) and j < len(l2):
            if l1[i] < l2[j]:
                res.append(l1[i])
                i += 1
            else:
                res.append(l2[j])
                j += 1
        res.extend(l1[i:])
        res.extend(l2[j:])
        GrahamPoint.center = prev_center
        return CyclicList(res)

    @staticmethod
    def is_anchor_point(polygon_point, center_point):
        return Point.check_is_turn_ccw(polygon_point.prev.data, center_point, polygon_point.data) == \
               Point.check_is_turn_ccw(polygon_point.next.data, center_point, polygon_point.data)

    @staticmethod
    def all_anchor_points(center, points):
        return list(filter(lambda point: DivideAndConquer.is_anchor_point(point, center), points.iter_nodes()))

if __name__=="__main__":
    l1 = list(range(0, 80, 5))
    l2 = list(range(0, 40, 7))
    res = DivideAndConquer.merge_lists(l1, l2)
    print(res)