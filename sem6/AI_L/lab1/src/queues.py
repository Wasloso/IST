from collections import deque
import heapq


from typing import Generic, TypeVar

T = TypeVar("T")


class PriorityQueue(Generic[T]):
    def __init__(self):
        self.elements: list[T] = []

    def empty(self) -> bool:
        return not self.elements

    def put(self, item: T, priority: int) -> None:
        heapq.heappush(self.elements, (priority, item))

    def get(self) -> T:
        return heapq.heappop(self.elements)[1]


class Queue(Generic[T]):
    def __init__(self, elements=None, maxlen: int = None):
        self.elements: deque[T] = deque(maxlen=maxlen)
        if elements:
            self.elements.extend(elements)

    def empty(self) -> bool:
        return not self.elements

    def put(self, item: T) -> None:
        self.elements.append(item)

    def get(self) -> T:
        return self.elements.popleft()
