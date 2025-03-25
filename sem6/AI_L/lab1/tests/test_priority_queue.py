import pytest
from src.priority_queue import (
    PriorityQueue,
)  #


def test_empty_queue():
    pq = PriorityQueue()
    assert pq.empty() is True


def test_put_and_not_empty():
    pq = PriorityQueue()
    pq.put("item1", priority=5)
    assert pq.empty() is False


def test_get_returns_correct_order():
    pq = PriorityQueue()
    pq.put("low_priority", priority=10)
    pq.put("high_priority", priority=1)
    pq.put("medium_priority", priority=5)

    assert pq.get() == "high_priority"
    assert pq.get() == "medium_priority"
    assert pq.get() == "low_priority"


def test_get_on_empty_raises_IndexError():
    pq = PriorityQueue()
    with pytest.raises(IndexError):
        pq.get()


def test_can_handle_multiple_same_priority():
    pq = PriorityQueue()
    pq.put("item1", priority=3)
    pq.put("item2", priority=3)
    pq.put("item3", priority=1)

    # item3 should come first
    assert pq.get() == "item3"
    # next two come in the order they were inserted (heapq behavior)
    assert pq.get() in ["item1", "item2"]
    assert pq.get() in ["item1", "item2"]


if __name__ == "__main__":
    pytest.main()
