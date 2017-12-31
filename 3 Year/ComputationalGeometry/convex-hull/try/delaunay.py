import time
import tkinter as tk

from build_digram import Subdivision
from primitives import Line, Point, Triangle

from convex_hull.algorithm import Algorithm


class Delaunay(Algorithm):
    def __init__(self, canvas=None, animate=False):
        super().__init__(canvas, animate)
        self.points = []
        self.triangles = []
        self.bounding_box = []

    def draw(self):
        self.canvas.delete(tk.ALL)
        for point in self.points:
            point.draw(self.canvas)
        for triangle in self.triangles:
            triangle.draw(self.canvas)
        self.canvas.update()

    def start(self, points):
        self.points = points
        self.triangles = []
        self.bounding_box = self.build_bounding_box(points)
        s = Subdivision(*self.bounding_box)

        for point in points:
            s.insert_site(point)

            if self.animate:
                self.triangles = self.get_triangles(s.triangulation.edges[0].edges[0])
                self.draw()
                time.sleep(0.1)
        return self.get_triangles(s.triangulation.edges[0].edges[0])

    def get_triangles(self, edge):
        output_edges = {}
        output_triangles = []

        stack = [edge]

        while len(stack):
            edge = stack.pop()
            if edge.next_edge not in output_edges:
                stack.append(edge.next_edge)
            if edge.sym().next_edge not in output_edges:
                stack.append(edge.sym().next_edge)

            self.add_triangle(edge, output_triangles, output_edges)
            self.add_triangle(edge.sym(), output_triangles, output_edges)

            output_edges[edge] = True
            output_edges[edge.sym()] = True

        return output_triangles

    def add_triangle(self, edge, triangles, edges):
        if edge in edges:
            return
        v1 = edge.origin
        edge = edge.right_next()
        if edge in edges:
            return
        v2 = edge.origin
        edge = edge.right_next()
        if edge in edges:
            return
        v3 = edge.origin
        if v1 in self.bounding_box or v2 in self.bounding_box or v3 in self.bounding_box:
            return
        triangles.append(Triangle(Point(v1.x, v1.y), Point(v2.x, v2.y), Point(v3.x, v3.y)))

    @classmethod
    def build_bounding_box(cls, points):
        bounding_box = []
        max_x = max(points, key=lambda point: point.x).x
        max_y = max(points, key=lambda point: point.y).y
        min_x = min(points, key=lambda point: point.x).x
        min_y = min(points, key=lambda point: point.y).y

        l1 = Line(min_x - 100, max_y + 100, 1 / 2.0)
        l2 = Line(max_x + 100, max_y + 100, -1 / 2.0)
        l3 = Line(min_x - 100, min_y - 100, 0)

        p1 = Point(l1.intersection(l2), l1.slope * l1.intersection(l2) + l1.intersect)
        p2 = Point(l1.intersection(l3), min_y - 100)
        p3 = Point(l2.intersection(l3), min_y - 100)

        bounding_box.append(p1)
        bounding_box.append(p2)
        bounding_box.append(p3)

        return bounding_box
