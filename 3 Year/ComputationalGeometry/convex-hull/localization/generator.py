from random import randint, choice
from edge import Point, Node, Edge


class Generator:
    def __init__(self, x0=0, y0=0, width=800, height=600):
        self.x0 = x0
        self.y0 = y0
        self.width = width
        self.height = height
        self.graph = []

    def generate(self):
        raise NotImplemented()

    def intersect_other(self, from_node, to_node):
        for node in self.graph:
            for adj in node.adj:
                if Edge.points_intersect(from_node, to_node, node, adj):
                    if not from_node == node and not from_node == adj and not to_node == node and not to_node == adj:
                        return True
        return False


class PlanarGenerator(Generator):
    points_range = (5, 20)
    width, height = 800, 600

    def generate(self):
        self.graph.clear()
        point_num = randint(*self.points_range)
        for i in range(point_num):
            point = Point(randint(0, self.width), randint(0, self.height))
            node = Node(point.x, point.y)
            self.graph.append(node)

        rib_num = 3 * point_num - 6 # some smart formula
        num_of_ribs = 0
        counter = 0
        while counter < 1000*point_num and num_of_ribs < rib_num:
            from_node = choice(self.graph)
            to_node = choice(self.graph)
            if not from_node == to_node and not  self.intersect_other(from_node, to_node) \
                    and not from_node in to_node.adj:
                from_node.adj.add(to_node)
                to_node.adj.add(from_node)
                num_of_ribs += 1
            counter += 1
        return self.graph

    def draw(self, canvas, color="blue"):
        for point in self.graph:
            point.draw(canvas, color=color)
            for other in point.adj:
                canvas.create_line(point.x, point.y, other.x, other.y, fill=color)
        canvas.update()



