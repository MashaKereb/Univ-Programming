from math import sqrt
from random import choice

class Point:
    vertex_radius = 3

    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y
        self.p = None

    def __hash__(self):
        return self.x.__hash__() | self.y.__hash__()

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __lt__(self, other):
        """
        Points are sorted by y-coordinate
        """
        return self.y < other.y or other.y == self.y and self.x < other.x

    @staticmethod
    def midpoint(point1, point2):
        x = (point1.x + point2.x) / 2
        y = (point1.y + point2.y) / 2
        return Point(x, y)

    def horizontal_intersection(self, a1, a2):
        if a1.x == a2.x:
            return Point(a1.x, self.y)

        k = (a2.y-a1.y)/(a2.x-a1.x)
        b = - k*a1.x + a1.y
        x = (self.y-b)/k
        return Point(x, self.y)

    def __str__(self):
        return "Point({}, {})".format(self.x, self.y)

    def draw(self, canvas, *, color="black", radius=1):
        if self.p is None:
            self.p = canvas.create_oval(self.x - self.vertex_radius*radius,
                           self.y - self.vertex_radius*radius,
                           self.x + self.vertex_radius*radius,
                           self.y + self.vertex_radius*radius, fill=color)
        else:
            canvas.itemconfig(self.p, fill=color)
        return self.p

    def distance_to(self, other):
        return sqrt((self.x - other.x) * (self.x - other.x) + (self.y - other.y) * (self.y - other.y))

    def distance_to_line(self, p1, p2):
        divisor = sqrt((p2.y-p1.y)**2 + (p2.x-p1.x)**2)
        denominator = abs((p2.y-p1.y)*self.x-(p2.x-p1.x)*self.y+p2.x*p1.y-p2.y*p1.x)
        return denominator/divisor

    @staticmethod
    def find_center(points):
        if len(points) < 3:
            return None

        p1 = p2 = p3 = None
        while p1 == p2 or p2 == p3 or p3 == p1:
            p1 = choice(points)
            p2 = choice(points)
            p3 = choice(points)

        return Point((p1.x + p2.x + p3.x) / 3, (p1.y + p2.y + p3.y) / 3)

    @staticmethod
    def check_is_turn_ccw(a, b, c):
        """
        Is a->b->c a counterclockwise turn?

       :param a first point
       :param b second point
       :param c third point
       :return: { False, None, True } if a->b->c is a { clockwise, collinear; counterclockwise } turn.
        """

        area = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
        if area == 0:
            return None
        else:
            return area > 0
