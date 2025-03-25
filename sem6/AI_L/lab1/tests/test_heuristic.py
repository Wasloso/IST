import pytest
import pandas as pd
from math import isclose
from src.astar import heuristic


def test_heuristic_same_point():
    pos_start = (0.0, 0.0)
    pos_end = (0.0, 0.0)
    travel_speed = 10
    result = heuristic(pos_start, pos_end, travel_speed)
    assert result == pd.Timedelta(minutes=0)


def test_heuristic_different_points():
    pos_start = (52.2296756, 21.0122287)  # Warsaw
    pos_end = (41.8919300, 12.5113300)  # Rome
    travel_speed = 100
    result = heuristic(pos_start, pos_end, travel_speed)
    expected_minutes = (1317.135 / travel_speed) * 60  # Approximate distance in km
    assert isclose(result.total_seconds() / 60, expected_minutes, rel_tol=1e-2)


def test_heuristic_zero_speed():
    pos_start = (52.2296756, 21.0122287)
    pos_end = (41.8919300, 12.5113300)
    travel_speed = 0
    with pytest.raises(ZeroDivisionError):
        heuristic(pos_start, pos_end, travel_speed)


def test_heuristic_negative_speed():
    pos_start = (52.2296756, 21.0122287)
    pos_end = (41.8919300, 12.5113300)
    travel_speed = -10
    result = heuristic(pos_start, pos_end, travel_speed)
    expected_minutes = (1317.135 / abs(travel_speed)) * 60
    assert isclose(result.total_seconds() / 60, expected_minutes, rel_tol=1e-2)
