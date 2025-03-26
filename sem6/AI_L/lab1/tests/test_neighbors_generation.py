import pytest
from src.tabu import generate_neighbours


def test_generate_neighbours_single_swap():
    path = ["A", "B", "C", "D"]
    expected = [["A", "B", "C", "D"], ["A", "C", "B", "D"]]
    assert generate_neighbours(path) == expected


def test_generate_neighbours_no_middle():
    path = ["A", "B"]
    expected = []
    assert generate_neighbours(path) == expected
