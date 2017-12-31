from point import Point
import math

class VoronoiEdge:
    """
        A class that stores an edge in Voronoi diagram

        start	: start point
        end		: end point
        left	: Voronoi place on the left side of edge
        right   : Voronoi place on the right side of edge
        m, b	: directional coefficients satisfying equation y = m*x + b (edge lies on this line)

        neighbour	: some edges consist of two parts, so we add the pointer
                      to another part to connect them at the end of an algorithm
        direction	: directional vector, from "start", points to "end", normal of |left, right|
    """
    def __init__(self, left_site, right_site):
        self.site1 = left_site
        self.site2 = right_site
        self.is_vertical = left_site.y == right_site.y
        if self.is_vertical:
            self.m = 0
            self.b = 0
        else:
            self.m = ((self.site2.x - self.site1.x)/(self.site1.y - self.site2.y))
            midpoint = Point.midpoint(self.site1, self.site2)
            self.b = midpoint.y - self.m*midpoint.x

        self.p1 = self.p2 = None

    def intersection(self, other):
        if self.m == other.m and self.b != other.b:
            return None  # no intersection
        if self.is_vertical:
            x = (self.site1.x + self.site2.x) / 2
            y = other.m * x + other.b
        elif other.is_vertical:
            x = (other.site1.x + other.site2.x) / 2
            y = self.m * x + self.b
        else:
            x = (other.b - self.b) / (self.m - other.m)
            y = self.m * x + self.b

        return Point(x, y)

    def draw(self, canvas, color="black"):

        canvas.create_line(self.site1.x, self.site1.y, self.site2.x, self.site2.y, fill=color)
