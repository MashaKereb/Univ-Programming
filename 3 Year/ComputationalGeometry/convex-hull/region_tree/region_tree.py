from convex_hull.algorithm import Algorithm
from blist import sortedlist

class RegionTreeSearch(Algorithm):
    class TreeNode:
        def __init__(self, left=0, right=0):
            self.left_x = 0
            self.right_x = 0
            self.left = left
            self.right = right
            self.points = sortedlist(key=lambda point: point.y)

    class Tree:
        def __init__(self, points):

            self.points = sorted(points, key=lambda point: point.x)
            self.root = RegionTreeSearch.TreeNode()
            for point in points:
                self.root.points.add(point)

            self.root.left_x = min(self.points, key=lambda point: point.x).x
            self.root.right_x = max(self.points, key=lambda point: point.x).x + 1
            self.build_tree(self.root)

            self.left_x = None
            self.right_x = None
            self.top_y = None
            self.bottom_y = None

        def build_tree(self, node):
            points = sorted(node.points, key=lambda x: x.x)
            l = len(node.points)

            if l > 1:
                node.left = RegionTreeSearch.TreeNode()
                for point in points[: l // 2]:
                    node.left.points.add(point)
                node.right = RegionTreeSearch.TreeNode()
                for point in points[l // 2:]:
                    node.right.points.add(point)

                node.left.left_x = node.left_x
                node.right.right_x = node.right_x

                node.left.right_x = max(node.left.points, key=lambda point: point.x).x
                node.right.left_x = min(node.left.points, key=lambda point: point.x).x

                self.build_tree(node.right)
                self.build_tree(node.left)

        def search(self, node, res_list):
            if len(node.points) > 1:
                if self.left_x <= node.left.right_x:
                    self.search(node.left, res_list)
                if self.right_x >= node.right.left_x:
                    self.search(node.right, res_list)

            else:
                point = node.points[0]
                if self.left_x <= point.x < self.right_x and \
                   self.bottom_y <= point.y <= self.top_y:
                    res_list.append(point)

    def __init__(self, canvas=None, animate=False):
        super().__init__(canvas, animate)
        self.tree = None
        self.points = []

    def get_points(self, point1, point2):

        self.tree.left_x = min(point1.x, point2.x)
        self.tree.right_x = max(point1.x, point2.x)
        self.tree.top_y = max(point1.y, point2.y)
        self.tree.bottom_y = min(point1.y, point2.y)
        res = []
        self.tree.search(self.tree.root, res)
        return res

    def start(self, points):
        self.points = points
        self.tree = RegionTreeSearch.Tree(self.points)

    def draw(self, points=None, color="violet"):
        if points is None:
            return []
        res = []
        for point in points:
            res.append(point.draw(self.canvas, color=color))
        self.canvas.update()
        return res

