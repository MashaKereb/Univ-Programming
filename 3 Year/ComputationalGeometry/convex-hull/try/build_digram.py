from primitives import QuadEdge
from checks import left_of, right_of, in_circle, valid


class Triangulation:
    def __init__(self):
        self.edges = []

    def make_edge(self):
        quad = QuadEdge()
        self.edges.append(quad)
        return quad.edges[0]

    def splice(self, a, b):
        t1 = b.origin_next()
        t2 = a.origin_next()
        alpha = t1.rotate()
        beta = t2.rotate()
        t3 = beta.origin_next()
        t4 = alpha.origin_next()

        a.next_edge = t1
        b.next_edge = t2
        alpha.next_edge = t3
        beta.next_edge = t4

    def delete_edge(self, edge):
        self.splice(edge, edge.origin_prev())
        self.splice(edge.sym(), edge.sym().origin_prev())
        self.edges.remove(edge.quad)

    def connect(self, a, b):
        e = self.make_edge()
        self.splice(e, a.left_next())
        self.splice(e.sym(), b)
        e.set_origin_dest(a.destination, b.origin)
        return e

    def swap(self, edge):
        e1 = edge.origin_prev()
        e2 = edge.sym().origin_prev()
        self.splice(edge, e1)
        self.splice(edge.sym(), e2)
        self.splice(edge, e1.left_next())
        self.splice(edge.sym(), e2.left_next())
        edge.set_origin_dest(e1.destination, e2.destination)


class Subdivision:
    def __init__(self, a, b, c):
        point_a = a
        point_b = b
        point_c = c

        self.triangulation = Triangulation()

        edge_a = self.triangulation.make_edge()
        edge_a.set_origin_dest(point_a, point_b)

        edge_b = self.triangulation.make_edge()
        self.triangulation.splice(edge_a.sym(), edge_b)
        edge_b.set_origin_dest(point_b, point_c)

        edge_c = self.triangulation.make_edge()
        self.triangulation.splice(edge_b.sym(), edge_c)
        edge_c.set_origin_dest(point_c, point_a)

        self.triangulation.splice(edge_c.sym(), edge_a)
        self.startingEdge = edge_a


    def locate(self, point):

        edge = self.startingEdge
        while True:
            if point == edge.origin or point == edge.destination:
                return edge
            elif right_of(point, edge):
                edge = edge.sym()
            elif not right_of(point, edge.origin_next()):
                edge = edge.origin_next()
            elif not right_of(point, edge.destination_prev()):
                edge = edge.destination_prev()
            else:
                return edge

    def insert_site(self, point):
        e = self.locate(point)
        conflicting_points = e.data

        if conflicting_points is None:
            conflicting_points = []

        base = self.triangulation.make_edge()
        first = e.origin
        base.set_origin_dest(first, point)
        self.triangulation.splice(base, e)

        while True:
            base = self.triangulation.connect(e, base.sym())
            e = base.origin_prev()
            if e.destination == first:
                break

        e = base.origin_prev()

        while True:
            t = e.origin_prev()
            if right_of(t.destination, e) and in_circle(e.origin, t.destination, e.destination, point):
                ed1 = e.sym()
                ed2 = ed1.f_next()
                ed3 = ed2.f_next()
                conflicting_points.extend(ed1.data)
                conflicting_points.extend(ed2.data)
                conflicting_points.extend(ed3.data)
                self.triangulation.swap(e)
                e = e.origin_prev()
            elif e.origin == first:
                conflict_triangles = get_triangles(base.sym())

                for triangle in conflict_triangles:
                    t = triangle[0]
                    t2 = triangle[2]
                    b = t.origin
                    c = t.destination
                    bb = t2.origin
                    cc = t2.destination
                    for point in conflicting_points:
                        if ((b.x - point.x) * (c.y - point.y) - (b.y - point.y) * (c.x - point.x)) > 0 and (
                                        (bb.x - point.x) * (cc.y - point.y) - (bb.y - point.y) * (cc.x - point.x)) > 0:
                            t.data.append(point)
                            point.conflicting_edge = t
                return
            else:
                e = e.origin_next().left_prev()


def get_triangles(e):
    triangles = []
    starting_edge = e

    while True:
        face_edges = []
        tri_edge = starting_edge

        face_edges.append(tri_edge)
        tri_edge.data = []
        tri_edge = tri_edge.f_next()
        face_edges.append(tri_edge)
        tri_edge.data = []
        tri_edge = tri_edge.f_next()
        face_edges.append(tri_edge)
        tri_edge.data = []

        starting_edge = starting_edge.origin_next()
        triangles.append(face_edges)
        if starting_edge == e:
            return triangles
