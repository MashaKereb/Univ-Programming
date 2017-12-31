from priority_queue import PriorityQueue
from circle_event import Event, CircleEvent
from voronoi_edge import VoronoiEdge
from point import Point
from beach_line import Parabola
import tkinter as tk
from arc import Arc, ArcQuery
from time import sleep
from blist import sorteddict
from break_point import BreakPoint
import math


class Voronoi:
    width, height = 400, 300
    size = 400

    def __init__(self, points):
        self.points = points
        self.voronoi_edges = []
        self.break_points = set()
        self.event_queue = PriorityQueue()
        self.beach_line_root = None
        self.sweep_line_position = self.size
        self.arcs = sorteddict()

    def get_sweep_loc(self):
        return self.sweep_line_position

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

    def check_circle_event(self, arc):
        """
        :return:
        """
        circle_center = arc.check_circle()

        if circle_center is not None:
            radius = arc.site.distance_to(circle_center)
            circle_event_point = Point(circle_center.x, circle_center.y - radius)
            ce = CircleEvent(arc, circle_event_point, circle_center)
            self.arcs[arc] = ce
            self.event_queue.push(ce)

    def handle_circle_event(self, event):
        """
        :param event: circle event to be processed
        """
        arc_right = self.arcs.keys()[self.arcs.keys().bisect_right()]
        arc_left = self.arcs.keys()[self.arcs.keys().bisect_left() - 1]
        if arc_right is not None:
            false_ce = self.arcs[arc_right]
            if false_ce is not None:
                self.event_queue.remove_entry(false_ce)
                self.arcs[arc_right] = None
        if arc_left is not None:
            false_ce = self.arcs[arc_left]
            if false_ce is not None:
                self.event_queue.remove_entry(false_ce)
                self.arcs[arc_left] = None

        del self.arcs[event.arc]
        event.arc.left.finish(event.vert)
        event.arc.right.finish(event.vert)
        self.break_points.remove(event.arc.left)
        self.break_points.remove(event.arc.right)
        e = VoronoiEdge(event.arc.left.s1, event.acr.right.s2)
        self.voronoi_edges.append(e)

        turns_left = Point.check_is_turn_ccw(arc_left.right.edge_begin, event.point, arc_right.left.edge_begin)
        is_left_point = e.m < 0 if turns_left else  e.m > 0
        if is_left_point:
            e.p1 = event.vert
        else:
            event.p2 = event.vert
        new_break = BreakPoint(event.arc.left.s1, event.arc.right.s2, e, not is_left_point, self)
        self.break_points.append(new_break)

        arc_left.right = new_break
        arc_right.left = new_break

        self.check_circle_event(arc_right)
        self.check_circle_event(arc_left)

    def handle_site_event(self, event):
        """
        :param event:
        """
        root = self.beach_line_root

        if root is None:
            # Deal with first point case
            arc = Arc(site=event.point, voronoi=self)
            self.beach_line_root = Parabola(site=event.point)
            self.arcs[arc] = None
            return

        # Find the arc above the site
        arc_above = self.arcs.keys()[self.arcs.keys().bisect_right(ArcQuery(event.point)) - 1]

        # Deal with the degenerate case where the first two points are at the same y value

        if len(self.arcs) == 1 and arc_above.site.y == event.point.y:
            new_edge = VoronoiEdge(arc_above.site, event.point)
            new_edge.p1 = Point((event.point.x + arc_above.site.x) / 2, math.inf)
            new_break = BreakPoint(arc_above.site, event.point, new_edge, False, self)
            self.break_points.append(new_break)
            self.voronoi_edges.append(new_edge)
            arc_left = Arc(None, new_break, voronoi=self)
            arc_right = Arc(new_break, None, voronoi=self)
            del self.arcs[arc_above]
            self.arcs[arc_left] = None
            self.arcs[arc_right] = None
            return

        # Remove the circle event associated with this arc if there is one
        falseCE = self.arcs[arc_above]
        if falseCE is not None:
            self.event_queue.remove_entry(falseCE)

        break_l = arc_above.left
        break_r = arc_above.right
        new_edge = VoronoiEdge(arc_above.site, event.point)
        self.voronoi_edges.append(new_edge)
        new_break_l = BreakPoint(arc_above.site, event.point, new_edge, True, self)
        new_break_r = BreakPoint(event.point, arc_above.site, new_edge, False, self)
        self.break_points.append(new_break_l)
        self.break_points.append(new_break_r)
        arc_left = Arc(break_l, new_break_l, voronoi=self)
        center = Arc(new_break_l, new_break_r, voronoi=self)
        arc_right = Arc(new_break_r, break_r, voronoi=self)

        del self.arcs[arc_above]
        self.arcs[arc_right] = None
        self.arcs[arc_left] = None
        self.arcs[center] = None

        self.check_circle_event(arc_left)
        self.check_circle_event(arc_right)

    def show(self, canvas):
        canvas.delete(tk.ALL)
        for point in self.points:
            point.draw(canvas, color="red")
        for edge in self.voronoi_edges:
            edge.draw(canvas)

    def draw(self, canvas):
        canvas.delete(tk.ALL)
        for point in self.points:
            point.draw(canvas, color="red")

        for point in self.break_points:
            point.draw(canvas)

        for arc in self.arcs.keys():
            arc.draw(canvas, color="gray")

        for edge in self.voronoi_edges:
            edge.draw(canvas, color="blue")

        # for arc in self.arcs.keys():
        #     arc.draw(canvas, color="green")
        canvas.create_line(0, self.sweep_line_position, self.width, self.sweep_line_position)
        canvas.update()
