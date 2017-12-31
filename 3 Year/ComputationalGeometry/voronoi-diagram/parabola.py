from utils import drange

class Parabola:
    def __init__(self, focus, directrix):
        self.focus = focus
        self.directrix = directrix

    def draw(self, min, max, canvas,  color="black"):
        a = self.focus.x
        b = self.focus.y
        c = self.directrix
        if b - c < 0.0000001:
            return
        prev = None
        for x in drange(min, max, 0.001):
            y = ((x - a) * (x - a) + (b * b) - (c * c)) / (2 * (b - c))
            if prev is not None:
                canvas.create_line(prev[0], prev[1], x, y, fill=color)
            prev = x, y