import pytest
from datetime import datetime, timedelta
from src.connection import Connection


# Mock function for calculate_wait_time
def mock_calculate_wait_time(start, end):
    return (end - start).total_seconds() / 60  # Return time in minutes


@pytest.fixture
def mock_utils(monkeypatch):
    from src import utils

    monkeypatch.setattr(utils, "calculate_wait_time", mock_calculate_wait_time)


def test_connection_initialization():
    departure = datetime(2023, 3, 1, 8, 0)
    arrival = datetime(2023, 3, 1, 10, 0)
    connection = Connection(
        company="TestCompany",
        line="Line1",
        departure_time=departure,
        arrival_time=arrival,
        start="Station A",
        stop="Station B",
    )

    assert connection.company == "TestCompany"
    assert connection.line == "Line1"
    assert connection.departure_time == departure
    assert connection.arrival_time == arrival
    assert connection.start_name == "Station A"
    assert connection.stop_name == "Station B"


def test_travel_time(mock_utils):
    departure = datetime(2023, 3, 1, 8, 0)
    arrival = datetime(2023, 3, 1, 10, 0)
    connection = Connection(
        company="TestCompany",
        line="Line1",
        departure_time=departure,
        arrival_time=arrival,
    )

    assert connection.travel_time == 120  # 2 hours in minutes


def test_repr():
    departure = datetime(2023, 3, 1, 8, 0)
    arrival = datetime(2023, 3, 1, 10, 0)
    connection = Connection(
        company="TestCompany",
        line="Line1",
        departure_time=departure,
        arrival_time=arrival,
        start="Station A",
        stop="Station B",
    )

    expected_repr = "(Line1) 08:00:00 Station A -> 10:00:00 Station B"
    assert repr(connection) == expected_repr
