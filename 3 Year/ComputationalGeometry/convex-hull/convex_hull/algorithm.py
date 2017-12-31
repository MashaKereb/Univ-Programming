class Algorithm:
    def __init__(self, canvas=None, animate=False):
        self.animate = animate and canvas is not None
        self.canvas = canvas

    def start(self, points):
        raise NotImplemented()

    def draw(self):
        raise NotImplemented()