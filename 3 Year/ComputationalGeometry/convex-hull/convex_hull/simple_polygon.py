from convex_hull.cyclic_list import CyclicList


class SimplePolygon:
    def __init__(self, canvas=None, animate=False):
        self.animate = animate and canvas is not None
        self.canvas = canvas

        self.points = CyclicList()

    def init(self, points):
        self.points = CyclicList()
        for point in points:
            self.points.add(point)
        if self.animate:
            self.draw()

    @staticmethod
    def check_if_intersect(point, a1, a2):
        return a1.y > point.y > a2.y or a2.y > point.y > a1.y

    @staticmethod
    def if_in_the_same_line(point, a1, a2):
        return a1.y == point.y == a2.y

    @staticmethod
    def goes_through_higher_point(point, a1, a2):
        y = min(a1.y, a2.y)  # min - because y are descending
        return y == point.y

    def localize(self, point):
        count = 0
        for p in self.points.iter_nodes():
            if self.check_if_intersect(point, p.data, p.next.data) or \
                    self.goes_through_higher_point(point, p.data, p.next.data):
                if self.if_in_the_same_line(point, p.data, p.next.data):
                    continue
                intersection = point.horizontal_intersection(p.data, p.next.data)
                if intersection.x <= point.x:
                    count += 1

        return count % 2 != 0

    def draw(self, color="black"):

        for point in self.points.iter_nodes():
            point.data.draw(self.canvas, color=color)
            self.canvas.create_line(point.data.x, point.data.y, point.next.data.x, point.next.data.y, fill=color)
        self.canvas.update()
