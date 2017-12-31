def triangle_area(a, b, c):
    return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)


def in_circle(a, b, c, d):
    return ((a.x * a.x + a.y * a.y) * ((c.x - b.x) * (d.y - b.y) - (c.y - b.y) * (d.x - b.x)) -
            (b.x * b.x + b.y * b.y) * ((c.x - a.x) * (d.y - a.y) - (c.y - a.y) * (d.x - a.x)) +
            (c.x * c.x + c.y * c.y) * ((b.x - a.x) * (d.y - a.y) - (b.y - a.y) * (d.x - a.x)) -
            (d.x * d.x + d.y * d.y) * ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x))) > 0


def ccw(a, b, c):
    return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0


def right_of(x, e):
    b = e.destination
    c = e.origin
    return (b.x - x.x) * (c.y - x.y) - (b.y - x.y) * (c.x - x.x) > 0


def left_of(x, e):
    b = e.origin
    c = e.destination
    return (b.x - x.x) * (c.y - x.y) - (b.y - x.y) * (c.x - x.x) > 0


def valid(e, basel):
    return not left_of(e.destination, basel)
