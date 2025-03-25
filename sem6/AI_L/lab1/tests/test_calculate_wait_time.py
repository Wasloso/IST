import pytest
import pandas as pd
from src.utils import calculate_wait_time


def test_calculate_wait_time_same_day():
    arrival_time = pd.Timestamp("1990-03-01 08:00:00")
    departure_time = pd.Timestamp("1990-03-01 10:00:00")
    assert calculate_wait_time(arrival_time, departure_time) == 120.0


def test_calculate_wait_time_next_day():
    arrival_time = pd.Timestamp("1990-03-01 22:00:00")
    departure_time = pd.Timestamp("1990-03-02 01:00:00")
    assert calculate_wait_time(arrival_time, departure_time) == 180.0


def test_calculate_wait_time_midnight_crossing():
    arrival_time = pd.Timestamp("1990-03-01 23:30:00")
    departure_time = pd.Timestamp("1990-03-02 00:15:00")
    assert calculate_wait_time(arrival_time, departure_time) == 45.0


def test_calculate_wait_time_no_wait():
    arrival_time = pd.Timestamp("1990-03-01 08:00:00")
    departure_time = pd.Timestamp("1990-03-01 08:00:00")
    assert calculate_wait_time(arrival_time, departure_time) == 0.0


def test_calculate_wait_time_two_midnights():
    arrival_time = pd.Timestamp("1990-03-01 23:00:00")
    departure_time = pd.Timestamp("1990-03-03 01:00:00")
    assert calculate_wait_time(arrival_time, departure_time) == 1560.0
