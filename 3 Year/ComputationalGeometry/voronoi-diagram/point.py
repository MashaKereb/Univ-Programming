from math import sqrt


class Point:
    vertex_radius = 2

    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y

    def __hash__(self):
        return self.x.__hash__() | self.y.__hash__()

    def __eq__(self, other):
        return self.x, self.y == other.x, other.y

    def __lt__(self, other):
        """
        Points are sorted by x-coordinate
        """
        return self.x, self.y < other.x, other.y

    @staticmethod
    def midpoint(point1, point2):
        x = (point1.x + point2.x) / 2
        y = (point1.y + point2.y) / 2
        return Point(x, y)

    def __str__(self):
        return "Point({}, {})".format(self.x, self.y)

    def draw(self, canvas, *, color="black"):
        canvas.create_oval(self.x - self.vertex_radius, self.y - self.vertex_radius, self.x + self.vertex_radius,
                           self.y + self.vertex_radius, fill=color)

    def distance_to(self, other):
        return sqrt((self.x - other.x) * (self.x - other.x) + (self.y - other.y) * (self.y - other.y))

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

