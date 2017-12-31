class Event:
    def __init__(self, point):
        self.point = point

    def __eq__(self, other):
        return self.point == other.point

    def __lt__(self, other):
        return self.point.y, self.point.x < other.point.y, self.point.x

    def draw(self, canvas, *, color="green"):
        self.point.draw(canvas, color=color, radius=2)

    def __hash__(self):
        return self.point.__hash__()


class CircleEvent(Event):
    def __init__(self, parabola, point, vert):
        super().__init__(point)
        self.arc = parabola
        self.vert = vert

    def draw(self, canvas, *, color="green"):
        self.point.draw(canvas, color=color)
        radius = self.vert.y - self.point.y
        canvas.create_oval.circle(
            self.vert.x - radius,
            self.vert.y - radius,
            self.vert.x + radius,
            self.vert.y + radius
        )

