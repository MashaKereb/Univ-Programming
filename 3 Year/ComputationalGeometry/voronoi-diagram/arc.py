import math
from point import Point
from parabola import Parabola

class ArcKey:
    def __init__(self):
        self.is_query = False

    def get_right(self):
        raise NotImplemented()

    def get_left(self):
        raise NotImplemented()

    def __eq__(self, other):
        s_left = self.get_left()
        s_right = self.get_right()

        o_left = other.get_left()
        o_right = other.get_right()

        return ((other.is_query or self.is_query) and
                ((s_left.x <= o_left.x and s_right.x >= o_right.x) or
                 (o_left.x <= s_left.x and o_right.x >= s_right.x))) or \
               (s_left.x == o_left.x and s_right.x == o_right.x) or \
               Point.midpoint(s_left, s_right) == Point.midpoint(o_left, o_right)

    def __lt__(self, other):
        s_left = self.get_left()
        s_right = self.get_right()

        o_left = other.get_left()
        o_right = other.get_right()

        return (s_right.x <= o_left.x or
                Point.midpoint(s_left, s_right) < Point.midpoint(o_left, o_right)) and not self == other


class Arc(ArcKey):
    def __init__(self, left=None, right=None, *, voronoi, site=None):
        super().__init__()
        if site is None:
            self.site = left.s2 if left is not None else right.s1
        else:
            self.site = site  # Only for creating the first Arc
        self.left = left
        self.right = right
        self.voronoi = voronoi

    def __hash__(self):
        return self.left.__hash__() | self.right.__hash__()

    def get_right(self):
        if self.right is not None:
            return self.right.get_point()
        return Point(math.inf, math.inf)

    def get_left(self):
        if self.left is not None:
            return self.left.get_point()
        return Point(-math.inf, math.inf)

    def draw(self, canvas, *, color):

        l = self.get_left()
        r = self.get_right()

        par = Parabola(self.site, self.voronoi.get_sweep_loc())
        minimum = 0 if l.x < 0 else l.x
        maximum = self.voronoi.width if r.x > self.voronoi.width else r.x

        par.draw(minimum, maximum, canvas, color=color)

    def check_circle(self):
        if self.left is None or self.right is None:
            return None
        if Point.check_is_turn_ccw(self.left.s1, self.site, self.right.s2) != False:
            return None
        return self.left.edge.intersection(self.right.edge)


class ArcQuery(ArcKey):
    def __init__(self, point):
        super().__init__()
        self.is_query = True
        self.p = point

    def get_left(self):
        return self.p

    def get_right(self):
        return self.p
