import heapq
import itertools


class PriorityQueue:
    def __init__(self):
        self.queue = []
        self.entry_finder = {}
        self.counter = itertools.count()

    def push(self, item):
        # check for duplicate
        if item in self.entry_finder:
            return
        count = next(self.counter)
        # use point as a primary key (heapq in python is min-heap)
        entry = [item.point, count, item]
        self.entry_finder[item] = entry
        heapq.heappush(self.queue, entry)

    def remove_entry(self, item):
        entry = self.entry_finder.pop(item)
        entry[-1] = 'Removed'

    def pop(self):
        while self.queue:
            priority, count, item = heapq.heappop(self.queue)
            if item is not 'Removed':
                del self.entry_finder[item]
                return item
        raise KeyError('pop from an empty priority queue')

    def top(self):
        while self.queue:
            priority, count, item = heapq.heappop(self.queue)
            if item is not 'Removed':
                del self.entry_finder[item]
                self.push(item)
                return item
        raise KeyError('top from an empty priority queue')

    def empty(self):
        return not self.queue
