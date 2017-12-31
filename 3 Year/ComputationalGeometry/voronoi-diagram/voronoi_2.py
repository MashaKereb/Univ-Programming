from circle_event import CircleEvent, Event
from voronoi_edge import VoronoiEdge
from break_point import BreakPoint
from point import Point
from arc import Arc
import tkinter as tk
import math


class Voronoi:
    MIN_DRAW_DIM = -5
    MAX_DRAW_DIM = 5

    MAX_DIM = 10
    MIN_DIM = -10

    def __init__(self, sites, canvas, animate):
        self.sweep_loc = self.MAX_DIM

        self.edges = []
        self.break_points = set()
        self.arcs = {}
        self.events = set()
        self.sites = []
        for x, y in sites:
            point = Point(x, y)
            self.sites.append(point)
            self.events.add(Event(point))

        while len(self.sites) > 0:
            cur = self.sites[0]
            self.sweep_loc = cur.y
            if animate:
                self.draw(canvas)
            if isinstance(cur, CircleEvent):
                self.handle_circle_event(cur)
            else:
                self.handle_site_event(cur)
        self.sweep_loc = self.MIN_DIM
        for break_point in self.break_points:
            break_point.finish()

    def get_sweep_loc(self):
        return self.sweep_loc

    def handle_site_event(self, event):
        # Deal with first point case
        if len(self.arcs) == 0:
            self.arcs[Arc(site=event, voronoi=self)] = None
            return

        arc_above = None
        # Find the arc above the site
        for arc in self.arcs.keys():
            if self.arcs[arc].point == event.point:
                arc_above = arc

        # Deal with the degenerate case where the first two points are at the same y value
        if len(self.arcs) == 0 and arc_above.site.y == event.point.y:
            new_edge = VoronoiEdge(arc_above.site, event.point)
            new_edge.start = Point((event.point.x + arc_above.site.x) / 2, math.inf)
            new_bp = BreakPoint(arc_above.site, event.point, new_edge, False, self)
            self.break_points.add(new_bp)
            self.edges.append(new_edge)
            arc_left = Arc(None, new_bp, voronoi=self)
            arc_right = Arc(new_bp, None, voronoi=self)
            self.arcs.pop(arc_above, None)
            self.arcs[arc_left] = None
            self.arcs[arc_right] = None
            return

        # Remove the circle event associated with this arc if there is one
        falseCE = self.arcs[arc_above]
        if falseCE is not None:
            self.events.remove(falseCE)

        break_l = arc_above.left
        break_r = arc_above.right
        new_edge = VoronoiEdge(arc_above.site, event.point)
        self.edges.append(new_edge)
        new_break_l = BreakPoint(arc_above.site, event.point, new_edge, True, voronoi=self)
        new_break_r = BreakPoint(event.point, arc_above.site, new_edge, False, voronoi=self)
        self.break_points.add(new_break_l)
        self.break_points.add(new_break_r)

        arc_left = Arc(break_l, new_break_l, voronoi=self)
        center = Arc(new_break_l, new_break_r, voronoi=self)
        arc_right = Arc(new_break_r, break_r, voronoi=self)

        self.arcs.pop(arc_above, None)
        self.arcs[arc_left] = None
        self.arcs[center] = None
        self.arcs[arc_right] = None

        self.check_for_circle_event(arc_left)
        self.check_for_circle_event(arc_right)

    def handle_circle_event(self, ce):
        arc_right = self.arcs.higherKey(ce.arc)
        arc_left = self.arcs.lowerKey(ce.arc)
        if arc_right is not None:
            falseCe = self.arcs[arc_right]
            if falseCe is not None:
                self.events.remove(falseCe)
            self.arcs[arc_right] = None
        if arc_left is not None:
            falseCe = self.arcs[arc_left]
            if falseCe is not None:
                self.events.remove(falseCe)
            self.arcs[arc_left] = None

        self.arcs.pop(ce.arc, None)

        ce.arc.left.finish(ce.vert)
        ce.arc.right.finish(ce.vert)

        self.break_points.remove(ce.arc.left)
        self.break_points.remove(ce.arc.right)

        e = VoronoiEdge(ce.arc.left.left, ce.arc.right.right)
        self.edges.append(e)

        # Here we're trying to figure out if the Voronoi vertex we've found is the left
        # or right point of the  edge.
        # If the edges being traces out by these two self.arcs take a right turn then we know
        # that the vertex is going to be above the current point

        turns_left = Point.check_turn(arc_left.right.edge_begin, ce.point, arc_right.left.edge_begin) == 1
        # So if it turns left, we know the next vertex will be below this vertex
        # so if it's below and the slow is negative then this vertex is the left point
        is_left_point = (e.m < 0) if turns_left else (e.m > 0)
        if is_left_point:
            e.start = ce.vert
        else:
            e.end = ce.vert
        BP = BreakPoint(ce.arc.left.left, ce.arc.right.right, e, not is_left_point, voronoi=self)
        self.break_points.add(BP)

        arc_right.left = BP
        arc_left.right = BP

        self.check_for_circle_event(arc_left)
        self.check_for_circle_event(arc_right)

    def check_for_circle_event(self, arc):
        center = arc.check_circle()
        if center is not None:
            radius = arc.site.distance_to(center)
            circle_event_point = Point(center.x, center.y - radius)
            ce = CircleEvent(arc, circle_event_point, center)
            self.arcs[arc] = ce
            self.events.add(ce)

    def show(self, canvas):
        canvas.delete(tk.ALL)
        for point in self.sites:
            point.draw(canvas, color="red")
        for edge in self.edges:
            edge.draw(canvas)

    def draw(self, canvas):
        canvas.delete(tk.ALL)
        for point in self.sites:
            point.draw(canvas, color="red")
        for bp in self.break_points:
            bp.draw(canvas)
        for edge in self.edges:
            edge.draw(canvas)

        for arc in self.arcs.keys():
            arc.draw(canvas, color="green")
        canvas.create_line(self.MIN_DIM, self.sweep_loc, self.MAX_DIM, self.sweep_loc)
