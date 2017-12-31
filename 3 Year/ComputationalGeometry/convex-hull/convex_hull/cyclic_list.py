class CyclicList:
    class Node:
        def __init__(self, data):
            self.data = data
            self.next = self
            self.prev = self

        def __eq__(self, other):
            return self.data == other.data

        def __str__(self):
            return str(self.data)

    def __init__(self, iterable=None):
        self.start = None
        self.len = 0
        if iterable is not None:
            for item in iterable:
                self.add(item)

    def add(self, item):
        new = CyclicList.Node(item)
        if self.start is None:
            self.start = new
        else:
            last = self.start.prev
            last.next = new
            new.prev = last

            self.start.prev = new
            new.next = self.start
        self.len += 1

    def find_node(self, value):
        for item in self.iter_nodes():
            if item.data == value:
                return item
        return None

    def iter_nodes(self):
        if self is not None and self.start is not None:
            item = self.start
            yield item
            while self.start is not None and item != self.start.prev:
                item = item.next
                yield item
        raise StopIteration

    def __iter__(self):
        if self is not None and self.start is not None:
            item = self.start
            yield item.data
            while self.start is not None and item != self.start.prev:
                item = item.next
                yield item.data
        raise StopIteration

    def remove_node(self, node):
        if node is not None:
            node.prev.next = node.next
            node.next.prev = node.prev
            if node == self.start:
                self.start = node.next
                if node.next == node:
                    self.start = None
            self.len -= 1

    def remove(self, item):
        node = self.find_node(item)
        self.remove_node(node)

    def __len__(self):
        return self.len

    def __getitem__(self, item):
        start = self.start
        while item > 0 and start is not None:
            start = start.next
            item -= 1
        return start.data

    def normalize(self):
        self.start = self.find_node(min(self))

    def split(self):
        res1 = CyclicList()
        res2 = CyclicList()
        i = 0
        for item in self:
            if i % 2 == 0:
                res1.add(item)
            else:
                res2.add(item)
            i +=1
        return res1, res2

if __name__ == "__main__":

    a = CyclicList([2, 4, 5, 6, 7, 8, 9])
    for i in a:
        a.remove(i)
        print(a.start)
    print(list(a))
