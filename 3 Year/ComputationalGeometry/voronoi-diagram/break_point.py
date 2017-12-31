from point import Point
from math import sqrt


def sq(x):
    return x*x


class BreakPoint:
    def __init__(self, left, right, edge, is_edge_left, voronoi):
        self.cache_sweep_line_loc = None
        self.cache_point = None

        self.voronoi = voronoi
        self.s1 = left
        self.s2 = right
        self.edge = edge
        self.is_edge_left = is_edge_left
        self.edge_begin = self.get_point()


    def draw(self, canvas, color="blue"):
        p = self.get_point()
        p.draw(canvas, color=color)

        canvas.create_line(self.edge_begin.x, self.edge_begin.y, p.x, p.y, fill=color)
        if self.is_edge_left and self.edge.p2 is not None:
            canvas.create_line(self.edge_begin.x, self.edge_begin.y, self.edge.p2.x, self.edge.p2.y, fill="grey")
        elif not self.is_edge_left and self.edge.p1 is not None:
            canvas.create_line(self.edge_begin.x, self.edge_begin.y, self.edge.p1.x, self.edge.p1.y, fill="grey")


    def finish(self, vert=None):
        if vert is None:
            vert = self.get_point()
        if self.is_edge_left:
            self.edge.p1 = vert
        else:
            self.edge.p2 = vert

    def get_point(self):
        l = self.voronoi.get_sweep_loc()
        if l == self.cache_sweep_line_loc:
            return self.cache_point
        self.cache_sweep_line_loc = l

        # Handle the vertical line case
        if self.s1.y == self.s2.y:
            x = (self.s1.x + self.s2.x)/2; #x coordinate is between the two sites
            y = (sq(x - self.s1.x) + sq(self.s1.y) - sq(l)) / (2*self.s1.y - l)  #comes from parabola focus-directrix definition
        else:
            px = self.s1.x if self.s1.y > self.s2.y else self.s2.x
            py = self.s1.y if self.s1.y > self.s2.y else self.s2.y

            m = self.edge.m
            b = self.edge.b

            d = 2*(py - l)

            A = 1
            B = -2*px - d*m
            C = sq(px) + sq(py) - sq(l) - d*b
            sign = -1 if self.s1.y > self.s2.y else 1
            det = sq(B) - 4*A*C

            if det <= 0:
                #  When rounding leads to a very very small negative determinant, fix it
                x = -B/(2*A)
            else:
                x = (-B + sign*sqrt(det))/ (2*A)
            y = m*x + b
        self.cache_point = Point(x, y)
        return self.cache_point
