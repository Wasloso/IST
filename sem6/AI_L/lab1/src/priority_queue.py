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
