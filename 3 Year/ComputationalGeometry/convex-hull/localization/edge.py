from convex_hull.point import Point


class Node(Point):
    def __init__(self, x, y):
        super().__init__(x, y)
        self.adj = set()


class Edge:
    def __init__(self, start, end):
        self.start = start
        self.end = end

    def __hash__(self):
        return self.end.__hash__() | self.start.__hash__()

    def __eq__(self, other):
        return self.end == other.end and self.start == other.start

    def cross_product(self, point):
        return (self.end.x - self.start.x) * (point.y - self.start.y) \
               - (self.end.y - self.start.y) * (point.x - self.start.x)

    def value_in_y(self, y):
        return (y - self.start.y) * (self.end.x - self.start.x) / (self.end.y - self.start.y) + self.start.x

    def value_in_x(self, x):
        return (x - self.start.x) * (self.end.y - self.start.y) / (self.end.x - self.start.x) + self.start.y

    @staticmethod
    def orientation(a, b, c):
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x)

    @classmethod
    def points_intersect(cls, a, b, c, d):
        return cls.orientation(a, c, d) != cls.orientation(b, c, d) and \
               cls.orientation(a, b, c) != cls.orientation(a, b, d)

    @classmethod
    def edges_intersect(cls, e1, e2):
        return cls.orientation(e1.start, e2.start, e2.end) != cls.orientation(e1.end, e2.start, e2.end) and \
               cls.orientation(e1.start, e1.end, e2.start) != cls.orientation(e1.start, e1.end, e2.end)

    @classmethod
    def get_intersection_point(cls, e1, e2):
        if e1.start.y == e1.end.y:
            y = e1.start.y
            x = e2.value_in_y(y)
        elif e2.start.y == e2.end.y:
            y = e2.start.y
            x = e1.value_in_y(y)
        elif e1.start.x == e1.end.x:
            x = e1.start.x
            y = e2.value_in_x(x)
        elif e2.start.x == e2.end.x:
            x = e2.start.x
            y = e1.value_in_x(x)
        else:
            denominator = (e1.start.x - e1.end.x) * (e2.start.y - e2.end.y) \
                          - (e1.start.y - e1.end.y) * (e2.start.x - e2.end.x)
            if denominator == 0:
                return None  # lines are parallel
            x_numerator = (e1.start.x * e1.end.y - e1.start.y * e1.end.x) * (e2.start.x - e2.end.x) \
                          - (e1.start.x - e1.end.x) * (e2.start.x * e2.end.y - e2.start.y * e2.end.x)
            y_numerator = (e1.start.x * e1.end.y - e1.start.y * e1.end.x) * (e2.start.y - e2.end.y) \
                          - (e1.start.y - e1.end.y) * (e2.start.x * e2.end.y - e2.start.y * e2.end.x)
            x = x_numerator / denominator
            y = y_numerator / denominator

        return Point(x, y)

    def substitution_x(self, p):
        position_x = self.value_in_y(p.y)
        return int(p.x - position_x)


    def point_is_on_right_side(self, point):
        return self.cross_product(point) < 0

    def draw(self, canvas, color="res"):
        return canvas.create_line(self.start.x, self.start.y, self.end.x, self.end.y, fill=color)
