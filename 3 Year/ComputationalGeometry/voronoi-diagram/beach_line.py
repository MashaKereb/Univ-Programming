"""
    We can represent a borderline sequence as a doubly-linked list.
    Inserting and deleting items is easy, but searching an item takes a linear time.
    Steven Fortune came up with a great idea, to represent this sequence as a binary tree.
"""


class Parabola:
    """
    The binary tree that represents a borderline sequence of voronoi arcs (beach-line)

     * Each leaf in the tree represents an arc.

     * Each inner node is an edge between two arcs.

     * If there is only one arc, the tree is only one leaf  (root)

     * When we add a new arc into the tree (when a sweep line rolls over the new site),
       the "right leaf" (the arc under the site being added) splits into two half-arcs
       and a new arc is added between them.
       The "right leaf" becomes an inner node and gets 2 children.
       Left child represents a left half-arc of a previous arc. Right child is a subtree,
       which contains a newly added arc on the left and right half-arc of previous arc on the right.

     * Removing an arc is also very easy. Its parent represents the left or the right edge,
       some "higher predecesor" is the next edge. If currently removed arc is the left child,
       we replace the predecesor by the right child and vice versa.
       We attach the new edge to the "higher predecesor".

    """

    def __init__(self, site=None):
        """
        is_leaf		  : flag whether the node is Leaf or internal node
        site	   	  : focus point of parabola (when it is parabola)
        edge		  : edge (when it is an edge)
        circle_event  : event, when the arch disappears (circle event)
        parent		  : parent node in tree
        """
        self.is_leaf = site is not None
        self.site = site
        self.edge = None
        self.circle_event = None
        self.parent = None

        self.left = None
        self.right = None

    def set_left(self, left_parabola):
        self.left = left_parabola
        left_parabola.parent = self

    def set_right(self, right_parabola):
        self.left = right_parabola
        right_parabola.parent = self

    def get_left_parent(self):
        """
        :return: the closest parent which is on the left
        """
        parent = self.parent
        p_last = self
        while parent.left == p_last:
            if parent.parent is None:
                return None
            p_last = parent
            parent = parent.parent
        return parent

    def get_right_parent(self):
        """
        :return: the closest parent which is on the right
        """
        parent = self.parent
        p_last = self
        while parent.right == p_last:
            if parent.parent is None:
                return None
            p_last = parent
            parent = parent.parent
        return parent

    def get_right_child(self):
        """
        :return: the closest leave which is on the right of current node
        """
        parabola = self.right
        while not parabola.is_leaf:
            parabola = parabola.right
        return parabola

    def get_left_child(self):
        """
        :return: the closest leave which is on the left of current node
        """
        parabola = self.left
        while not parabola.is_leaf:
            parabola = parabola.left
        return parabola

    def get_left_leave(self):
        """
        :return: the closest left leave of the tree
        """
        return self.get_left_parent().get_left_child()

    def get_right_leave(self):
        """
        :return: the closest left leave of the tree
        """
        return self.get_right_parent().get_right_child()
