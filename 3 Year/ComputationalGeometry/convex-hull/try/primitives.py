class Point:
    vertex_radius = 3

    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.conflicting_edge = None

    def draw(self, canvas, *, color="black", radius=1):
        return canvas.create_oval(self.x - self.vertex_radius * radius,
                                  self.y - self.vertex_radius * radius,
                                  self.x + self.vertex_radius * radius,
                                  self.y + self.vertex_radius * radius, fill=color)

    def __repr__(self):
        return "(" + str(self.x) + "," + str(self.y) + ")"

    def __lt__(self, other):
        return self.x < other.x if self.x != other.x else self.y < other.y

    def __gt__(self, other):
        return self.x > other.x if self.x != other.x else self.y > other.y

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __le__(self, other):
        return self.x <= other.x if self.x != other.x else self.y <= other.y

    def __ge__(self, other):
        return self.x >= other.x if self.x != other.x else self.y >= other.y

    def __ne__(self, other):
        return self.x != other.x or self.y != other.y


class Triangle:
    def __init__(self, a, b, c):
        self.a = a
        self.b = b
        self.c = c

    def __eq__(self, other):
        return self.a == other.a or self.a == other.b or self.a == other.c and \
               self.b == other.a or self.b == other.b or self.b == other.c and \
               self.c == other.a or self.c == other.b or self.c == other.c

    def draw(self, canvas, color="green"):
        canvas.create_line(self.a.x, self.a.y, self.b.x, self.b.y, fill=color)
        canvas.create_line(self.b.x, self.b.y, self.c.x, self.c.y, fill=color)
        canvas.create_line(self.a.x, self.a.y, self.c.x, self.c.y, fill=color)


class Edge:
    def __init__(self, i=0, quad=None):
        self.num = i
        self.next_edge = None
        self.data = []
        self.quad = quad

    def __str__(self):
        return str(self.origin) + " -> " + str(self.destination)

    def __lt__(self, other):
        return self.origin < other.origin

    def rotate(self):
        #  Return the dual of the current edge, directed from its right to its left.
        return self.quad.edges[(self.num + 1) % 4]

    def inv_rotate(self):
        #  Return the dual of the current edge, directed from its left to its right
        return self.quad.edges[(self.num - 1) % 4]

    def sym(self):
        # Return the edge from the destination to the origin of the current edge.
        return self.quad.edges[(self.num + 2) % 4]

    def origin_next(self):
        # Return the next ccw edge around (from) the origin of the current edge.
        return self.next_edge

    def origin_prev(self):
        # Return the next cw edge around (from) the origin of the current edge.
        return self.quad.edges[(self.num + 1) % 4].next_edge.rotate()

    def destination_next(self):
        #  Return the next ccw edge around (into) the destination of the current edge
        return self.sym().origin_next().sym()

    def destination_prev(self):
        #  Return the next cw edge around (into) the destination of the current edge.
        return self.inv_rotate().origin_next().inv_rotate()

    def left_next(self):
        # Return the ccw edge around the left face following the current edge.
        return self.quad.edges[(self.num - 1) % 4].next_edge.rotate()

    def left_prev(self):
        # Return the ccw edge around the left face before the current edge.
        return self.origin_next().sym()

    def right_next(self):
        # Return the edge around the right face ccw following the current edge.
        return self.rotate().origin_next().inv_rotate()

    def right_prev(self):
        # Return the edge around the right face ccw before the current edge.
        return self.sym().origin_next()

    def origin(self):
        return self.data

    def destination(self):
        return self.sym().data

    def f_next(self):
        return self.sym().origin_prev()


    def set_origin_dest(self, origin, destination):
        self.origin = origin
        self.destination = destination
        self.sym().origin = destination
        self.sym().destination = origin


class QuadEdge:
    def __init__(self):
        e0 = Edge(0, self)
        e1 = Edge(1, self)
        e2 = Edge(2, self)
        e3 = Edge(3, self)

        e0.next_edge = e0
        e1.next_edge = e3
        e2.next_edge = e2
        e3.next_edge = e1
        self.edges = [e0, e1, e2, e3]


class Line:
    def __init__(self, x, y, slope):
        self.x = x
        self.y = y
        self.slope = slope
        self.intersect = self.y - self.slope * x

    def intersection(self, line):
        return (line.intersect - self.intersect) / (self.slope - line.slope)