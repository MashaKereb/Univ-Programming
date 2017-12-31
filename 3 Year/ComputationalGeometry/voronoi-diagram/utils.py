def drange(start, stop, step, epsilon=0.0000001):
    r = start
    while r < stop - epsilon:
        yield r
        r += step

def abs(x):
    if x >= 0:
        return x
    else:
        return -x