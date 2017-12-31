from edge import Edge, Node, Point
from time import sleep
from collections import defaultdict


class WeightedEdge(Edge):
    def __init__(self, start, end, weight=1):
        super().__init__(start, end)
        self.weight = weight


class Localizator:
    def __init__(self, graph):
        self.graph = graph

    @staticmethod
    def edges_to_points(bounds):
        if bounds is None:
            return None
        hull = []
        for i in range(len(bounds) - 1):
            hull.append(Edge.get_intersection_point(bounds[i], bounds[i + 1]))

        hull.append(Edge.get_intersection_point(bounds[0], bounds[-1]))
        return hull


class ChainLocalizator(Localizator):
    def __init__(self, graph, canvas=None):
        super().__init__(graph)
        self.canvas = canvas
        self.graph.sort(key=lambda point: (point.y, point.x))
        self.lowest_point = self.graph[0]
        self.highest_point = self.graph[-1]

        weighted_edges = self.get_weighted_edges(self.graph)
        self.chains = self.get_chains_from_weighted_edges(weighted_edges)

    @classmethod
    def get_weighted_edges(cls, graph):
        out_edges = []
        for origin in graph:
            for adj in origin.adj:
                if origin.y < adj.y:
                    out_edges.append(WeightedEdge(origin, adj))

        weights_in = [0] * len(graph)
        weights_out = [0] * len(graph)

        for i in range(1, len(graph) - 1):
            final_i = i
            filtered_by_end = list(filter(lambda edge: edge.end == graph[final_i], out_edges))
            filtered_by_start = list(filter(lambda edge: edge.start == graph[final_i], out_edges))
            weights_in[i] = (sum(map(lambda x: x.weight, filtered_by_end)))

            leftmost_out = min(filtered_by_start, key=cls.edge_comparator)

            v_out = len(filtered_by_start)
            if weights_in[i] > v_out:
                leftmost_out.weight = weights_in[i] - v_out + 1
                weights_in[graph.index(leftmost_out.end)] += leftmost_out.weight - 1
                weights_out[i] += leftmost_out.weight - 1

        for i in range(len(graph) - 2, 0, -1):
            final_i = i

            filtered_by_end = list(filter(lambda edge: edge.end == graph[final_i], out_edges))
            filtered_by_start = list(filter(lambda edge: edge.start == graph[final_i], out_edges))
            weights_out[i] = sum(map(lambda x: x.weight, filtered_by_start))

            leftmost_in = min(filtered_by_end, key=cls.edge_comparator)

            if weights_out[i] > weights_in[i]:
                leftmost_in.weight += weights_out[i] - weights_in[i]
                weights_in[i] += leftmost_in.weight - 1
                weights_out[graph.index(leftmost_in.start)] += leftmost_in.weight - 1
        return out_edges

    @classmethod
    def edge_comparator(cls, o1):
        upper_y = o1.start.y + 100  # 100 is arbitrary (but large enough) number
        o1_edge = Edge(o1.start, o1.end)
        return o1.start.y, o1.start.x, o1_edge.value_in_y(upper_y)

    def get_chains_from_weighted_edges(self, edges):
        res = []

        all_edges = []
        all_edges.extend(edges)

        while len(all_edges) > 0:
            curr_chain = []
            curr_edge = min(filter(lambda edge: edge.start == self.lowest_point, all_edges), key=self.edge_comparator)

            curr_chain.append(curr_edge)
            curr_edge.weight -= 1
            if curr_edge.weight == 0:
                all_edges.remove(curr_edge)

            while curr_edge.end != self.highest_point:
                #     curr_edge.draw(self.canvas, color="green")
                #     self.canvas.update()
                #     print(curr_edge.weight)
                #     sleep(1)
                prev_edge = curr_edge

                curr_edge = min(filter(lambda edge: edge.start == prev_edge.end, all_edges), key=self.edge_comparator)

                curr_edge.weight -= 1
                if curr_edge.weight == 0:
                    all_edges.remove(curr_edge)
                curr_chain.append(curr_edge)

            res.append(curr_chain)
        return res

    @staticmethod
    def point_is_on_right_to_chain(chain, point):
        appropriate_edge = None
        for edge in chain:
            if (point.y >= edge.start.y) and (point.y <= edge.end.y):
                appropriate_edge = edge
                break

        return appropriate_edge.point_is_on_right_side(point)

    def locate_point_between_chains(self, sortedChains, point):
        if len(sortedChains) < 2:
            return -1
        if point.y > self.highest_point.y:
            if point.x < self.highest_point.x:
                return None, 0
            else:
                return len(sortedChains) - 1, None
        if point.y < self.lowest_point.y:
            if point.x < self.lowest_point.x:
                return None, 0
            else:
                return len(sortedChains) - 1, None

        index = -1
        start = 0
        end = len(sortedChains) - 1
        while end != start:
            if self.point_is_on_right_to_chain(sortedChains[end], point):
                return end, None
            if not self.point_is_on_right_to_chain(sortedChains[start], point):
                return None, start
            left = sortedChains[start]
            right = sortedChains[start + 1]
            is_on_right_to_left_chain = self.point_is_on_right_to_chain(left, point)
            is_on_right_to_right_chain = self.point_is_on_right_to_chain(right, point)
            if is_on_right_to_left_chain and not is_on_right_to_right_chain:
                index = start
                break
            mid = (end + start) // 2
            if self.point_is_on_right_to_chain(sortedChains[mid], point):
                start = mid
            elif not self.point_is_on_right_to_chain(sortedChains[mid], point):
                end = mid
        return index, index + 1

    def get_region(self, point_to_locale):

        # d = defaultdict(int)
        # for edge in weightedEdges:
        #     edge.draw(self.canvas, color="red")
        #     edge.start.draw(self.canvas, color="violet", radius=5)
        #     edge.end.draw(self.canvas, color="red", radius=2)
        #     self.canvas.update()
        #     sleep(2)
        #     d[edge.start] += edge.weight
        # print(d)

        index_of_left_chain, index_of_right_chain = self.locate_point_between_chains(self.chains, point_to_locale)
        print(index_of_left_chain, index_of_right_chain)
        if index_of_left_chain == -1:
            return None

        left_chain = self.chains[index_of_left_chain] if index_of_left_chain is not None else None
        right_chain = self.chains[index_of_right_chain] if index_of_right_chain is not None else None

        return left_chain, right_chain

    def draw_chain(self, chain, canvas, color="red"):
        res = []
        if chain is None:
            chain = []
        for edge in chain:
            res.append(edge.draw(self.canvas, color=color))
            res.append(edge.start.draw(self.canvas, color=color, radius=2))
        canvas.update()
        return res


class StripLocalizator(Localizator):
    def __init__(self, graph, canvas=None):
        super().__init__(graph)
        self.canvas = canvas

        self.leftmost = min(graph, key=lambda point: point.x)
        self.rightmost = max(graph, key=lambda point: point.x)

        self.sets = self.compute_sets_of_edges()

    def compute_sets_of_edges(self):
        all_edges = set()
        for n in self.graph:
            for adj in n.adj:
                all_edges.add(Edge(n, adj))

        sets = []
        for i in range(len(self.graph)):
            sets.append(list())

        self.graph.sort(key=lambda p: (p.y, p.x))
        for i in range(1, len(self.graph)):
            # horizontal line that 1 unit further that canvas bounds
            left = Node(self.leftmost.x - 1, self.graph[i - 1].y)
            right = Node(self.rightmost.x + 1, self.graph[i - 1].y)
            horizon_edge = Edge(left, right)
            for edge in all_edges:
                if Edge.edges_intersect(horizon_edge, edge):
                    sets[i].append(edge)

        return sets

    def get_strip_index(self, point_to_locale):
        index = 0
        start = 0
        end = len(self.graph) - 1
        while end != start:
            if point_to_locale.y > self.graph[end].y:
                return -1
            if self.graph[start].y <= point_to_locale.y <= self.graph[start + 1].y:
                return start + 1
            elif self.graph[end - 1].y <= point_to_locale.y <= self.graph[end].y:
                return end

            mid = (end + start) // 2
            if point_to_locale.y > self.graph[mid].y:
                start = mid
            elif point_to_locale.y < self.graph[mid].y:
                end = mid
            else:
                return mid

        return index

    def get_edge_index(self, sorted_edges, point_to_locale):
        if len(sorted_edges) < 2:
            return -1

        index = -1
        start = 0
        end = len(sorted_edges) - 1
        while end != start:
            if sorted_edges[end].substitution_x(point_to_locale) > 0:
                return end - 1
            if sorted_edges[start].substitution_x(point_to_locale) < 0:
                return start
            left = sorted_edges[start]
            right = sorted_edges[start + 1]
            d1 = left.substitution_x(point_to_locale)
            d2 = right.substitution_x(point_to_locale)
            if d1 * d2 <= 0:
                index = start
                break
            mid = (end + start) // 2
            if sorted_edges[mid].substitution_x(point_to_locale) > 0:
                start = mid
            elif sorted_edges[mid].substitution_x(point_to_locale) < 0:
                end = mid
            else:
                index = mid
                break
        return index

    def get_region(self, point_to_locale):

        strip_index = self.get_strip_index(point_to_locale)
        if strip_index < 1:
            return None
        y_top = self.graph[strip_index].y
        y_bottom = self.graph[strip_index - 1].y
        sorted_edge_list = self.sets[strip_index]

        def comp(edge):
            top_x = edge.value_in_y(y_top)
            bot_x = edge.value_in_y(y_bottom)
            return top_x, bot_x

        sorted_edge_list.sort(key=comp)

        edge_index = self.get_edge_index(sorted_edge_list, point_to_locale)
        if edge_index == -1:
            print("-----------------------------")
            return None

        left_edge = sorted_edge_list[edge_index]
        top_edge = Edge(Point(self.leftmost.x, y_top), Point(self.rightmost.x, y_top))
        right_edge = sorted_edge_list[edge_index + 1]
        bottom_edge = Edge(Point(self.leftmost.x, y_bottom), Point(self.rightmost.x, y_bottom))

        return [left_edge, top_edge, right_edge, bottom_edge]

    def draw(self, edges, color="red"):
        res = []
        for edge in edges:
            res.append(edge.draw(self.canvas, color=color))
            res.append(edge.start.draw(self.canvas, color=color, radius=2))
            res.append(edge.end.draw(self.canvas, color=color, radius=2))
        rect = self.edges_to_points(edges)
        res.append(self.canvas.create_polygon(rect[0].x, rect[0].y,
                                              rect[1].x, rect[1].y,
                                              rect[2].x, rect[2].y,
                                              rect[3].x, rect[3].y,
                                              fill="pink"))
        return res
