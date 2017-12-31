from priority_queue import PriorityQueue
from circle_event import Event, CircleEvent
from voronoi_edge import VoronoiEdge
from point import Point
from beach_line import Parabola
import tkinter as tk
from arc import Arc
from time import sleep


class Voronoi:
    width, height = 400, 300

    def __init__(self, points):
        self.points = points
        self.voronoi_points = []
        self.voronoi_edges = []
        self.break_points = []
        self.event_queue = PriorityQueue()
        self.beach_line_root = None
        self.sweep_line_position = 0
        self.arcs = {}

    def start(self, canvas, animate=True):
        """
        :param canvas:
        :param animate:
        """

        for point in self.points:
            self.event_queue.push(Event(point))

        while not self.event_queue.empty():
            event = self.event_queue.pop()
         #   print(event.point, "  ==")
            self.sweep_line_position = event.point.y
            if animate:
                self.draw(canvas)
                event.draw(canvas)
                canvas.update()
                sleep(0.5)

            if isinstance(event, CircleEvent):
                self.handle_circle_event(event)
            else:
                self.handle_site_event(event)

        for b_point in self.break_points:
            b_point.finish()

        if animate:
            self.draw(canvas)

    def check_circle_event(self, parabola):
        """
        :return:
        """

        left_parent, right_parent = parabola.get_left_parent(), parabola.get_right_parent()
        if left_parent is None or right_parent is None:
            return
        l, r = left_parent.get_left_child(), right_parent.get_right_child()

        if l is None or r is None or r.site == l.site:
            return


        intersect_point = left_parent.edge.intersection(right_parent.edge)
        print("/////////////////////////////////////////")
        if intersect_point is None:
            return
        print("ppppppppppppppppppppppppppppppppppppppppppppppp")

        radius = intersect_point.distance_to(l.site)
        if intersect_point.y + radius > self.sweep_line_position:  # TODO: check the sign (direction of the sweep line)
            return

        self.voronoi_points.append(intersect_point)
        event = CircleEvent(
            parabola=parabola,
            center=intersect_point,
            point=Point(intersect_point.x, intersect_point.y + radius)
        )
        parabola.circle_event = event
        self.event_queue.push(event)

    def handle_circle_event(self, event):
        """
        :param event: circle event to be processed
        """
        print("---------------------------------------------------------------")
        parabola = event.arc
        left_parent, right_parent = parabola.get_left_parent(), parabola.get_right_parent()
        l, r = parabola.get_left_child(), parabola.get_right_child()
        if l.circle_event is not None:
            self.event_queue.remove_entry(l.circle_event)
            l.circle_event = None
        if r.circle_event is not None:
            self.event_queue.remove_entry(r.circle_event)
            r.circle_event = None

        # s = the circumcenter between l.site, p.site and r.site
        point = event.center
        other_p = Point(event.point.x,
                        self.y_coord_for_site_event(parabola.site, event.point.x))

        print(point, other_p, "  000000000000")
        self.voronoi_points.append(point)

        # finish two neighbour edges xl, xr at point s
        left_parent.edge.end = point
        right_parent.edge.end = point

        #  replace a sequence xl, p, xr by new edge x
        new_edge = VoronoiEdge(start=point, left_site=l.site, right_site=r.site)
        self.voronoi_edges.append(new_edge)

        par = higher = parabola

        while parabola != self.beach_line_root:
            par = par.parent
            if par == left_parent:
                higher = left_parent
            if par == right_parent:
                higher = right_parent
        higher.edge = new_edge

        gparent = parabola.parent.parent
        if parabola.parent.left == parabola:
            if gparent.left == parabola.parent:
                gparent.set_left(parabola.parent.right)
            if gparent.right == parabola.parent:
                gparent.set_right(parabola.parent.right)
        else:
            if gparent.left == parabola.parent:
                gparent.set_left(parabola.parent.left)
            if gparent.right == parabola.parent:
                gparent.set_right(parabola.parent.left)

        self.check_circle_event(l)
        self.check_circle_event(r)

    def handle_site_event(self, event):
        """
        :param event:
        """
        root = self.beach_line_root

        if root is None:
            # Deal with first point case
            arc = Arc(site=event.point, voronoi=self)
            self.beach_line_root = arc
            self.arcs[arc] = None
            return

        if root.is_leaf and event.point.y - root.site.y < 1:
            point = root.site
            root.is_leaf = False
            root.set_left(Parabola(point))
            root.set_right(Parabola(event.point))
            mid_point = Point((point.x + event.point.x)/2, 0)
            self.voronoi_points.append(mid_point)
            if point.x > event.point.x:
                edge = VoronoiEdge(mid_point, event.point, point)
            else:
                edge = VoronoiEdge(mid_point, point, event.point)
            root.edge = edge
            self.voronoi_edges.append(edge)
            return

        parabola = root.find_parabola_by_x_coord(event.point.x, self.sweep_line_position)
        print(parabola.site, " ****")
        if parabola.circle_event is not None:
            self.event_queue.remove_entry(parabola.circle_event)
            parabola.circle_event = None

        start = Point(event.point.x, self.y_coord_for_site_event(parabola.site, event.point.x))
        self.voronoi_points.append(start)

        left_edge = VoronoiEdge(start, parabola.site, event.point)
        right_edge = VoronoiEdge(start, event.point, parabola.site)

        left_edge.neighbour = right_edge
        # el->neighbour = er
        # edges->push_back(el) TODO: check

        self.voronoi_edges.append(left_edge)

        parabola.edge = right_edge
        parabola.is_leaf = False  # TODO: check if the parabola.site contains bigger point in the left sub-tree

        p0 = Parabola(parabola.site)
        p1 = Parabola(event.point)
        p2 = Parabola(parabola.site)

        parabola.set_right(p2)
        parabola.set_left(Parabola())
        parabola.left.edge = left_edge
        parabola.left.set_left(p0)
        parabola.left.set_right(p1)

        self.check_circle_event(p0)
        self.check_circle_event(p2)

    def y_coord_for_site_event(self, point, x_coord):
        # TODO: remove this function to Parabola class

        dp = 2 * (point.y - self.sweep_line_position)
        a1 = 1 / dp
        b1 = -2 * point.x * a1
        c1 = self.sweep_line_position + dp / 4 + point.x * point.x * a1

        return a1 * x_coord * x_coord + b1 * x_coord + c1

    def finish_edge(self, parabola):
        if parabola is None or parabola.is_leaf:
            return
        if parabola.edge.end is not None:
            self.finish_edge(parabola.left)
            self.finish_edge(parabola.right)

        if parabola.edge.direction.x > 0:
            mx = self.width + 5
        else:
            mx = -5
        end = Point(mx, mx*parabola.edge.m + parabola.edge.b)
        parabola.edge.end = end
        self.voronoi_points.append(end)
        self.finish_edge(parabola.left)
        self.finish_edge(parabola.right)

    def show(self, canvas):
        canvas.delete(tk.ALL)
        for point in self.voronoi_points:
            point.draw(canvas, color="red")
        for edge in self.voronoi_edges:
            edge.draw(canvas)

    def draw(self, canvas):
        canvas.delete(tk.ALL)
        for point in self.points:
            point.draw(canvas, color="red")

        for point in self.voronoi_points:
            point.draw(canvas, color="green")

        for edge in self.voronoi_edges:
            edge.draw(canvas, color="blue")

        # for arc in self.arcs.keys():
        #     arc.draw(canvas, color="green")
        canvas.create_line(0, self.sweep_line_position, self.width, self.sweep_line_position)
        canvas.update()

