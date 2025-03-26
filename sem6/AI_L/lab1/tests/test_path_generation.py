import pytest
import random

from src.tabu import initialize_path


def test_randomize_path_single_element():
    start = "A"
    to_visit = ["B"]
    result = initialize_path(start, to_visit)
    assert result[0] == start
    assert result[-1] == start
    assert set(result[1:-1]) == set(to_visit)


def test_randomize_path_multiple_elements():
    start = "A"
    to_visit = ["B", "C", "D"]
    result = initialize_path(start, to_visit)
    assert result[0] == start
    assert result[-1] == start
    assert set(result[1:-1]) == set(to_visit)
    assert len(result) == len(to_visit) + 2


def test_randomize_path_empty_to_visit():
    start = "A"
    to_visit = []
    result = initialize_path(start, to_visit)
    assert result == [start, start]


def test_randomize_path_randomness():
    start = "A"
    to_visit = ["B", "C", "D"]
    random.seed(42)  # Set seed for reproducibility
    result1 = initialize_path(start, to_visit[:])
    random.seed(42)
    result2 = initialize_path(start, to_visit[:])
    assert result1 == result2  # Ensure reproducibility with the same seed
    random.seed(None)  # Reset seed for randomness
