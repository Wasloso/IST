import pytest
import pandas as pd
from src.connection import Connection
from src.utils import calculate_wait_time
from src.utils import get_best_connection


@pytest.fixture
def sample_connections():
    return [
        Connection(
            company="Company1",
            line="A",
            departure_time=pd.Timestamp("1990-01-01 08:00:00"),
            arrival_time=pd.Timestamp("1990-01-01 08:05:00"),
        ),
        Connection(
            company="Company2",
            line="B",
            departure_time=pd.Timestamp("1990-01-01 08:10:00"),
            arrival_time=pd.Timestamp("1990-01-01 08:15:00"),
        ),
        Connection(
            company="Company1",
            line="A",
            departure_time=pd.Timestamp("1990-01-01 08:20:00"),
            arrival_time=pd.Timestamp("1990-01-01 08:25:00"),
        ),
        Connection(
            company="Company3",
            line="C",
            departure_time=pd.Timestamp("1990-01-01 08:30:00"),
            arrival_time=pd.Timestamp("1990-01-01 08:40:00"),
        ),
        Connection(
            company="Company4",
            line="D",
            departure_time=pd.Timestamp("1990-01-01 08:45:00"),
            arrival_time=pd.Timestamp("1990-01-01 08:55:00"),
        ),
        Connection(
            company="Company2",
            line="B",
            departure_time=pd.Timestamp("1990-01-01 09:00:00"),
            arrival_time=pd.Timestamp("1990-01-01 09:10:00"),
        ),
    ]


def test_get_best_connection_no_line_restriction(sample_connections, capsys):
    current_time = pd.Timestamp("1990-01-01 07:50:00")
    best_connection, best_cost = get_best_connection(current_time, sample_connections)
    assert best_connection is not None

    assert best_connection.line == "A"
    assert best_connection.departure_time == pd.Timestamp("1990-01-01 08:00:00")
    assert best_cost == 15


def test_get_best_connection_with_line_restriction(sample_connections):
    current_time = pd.Timestamp("1990-01-01 07:50:00")
    line_restriction = "B"
    best_connection, best_cost = get_best_connection(
        current_time, sample_connections, line=line_restriction
    )
    assert best_connection is not None

    assert best_connection.line == "B"
    assert best_connection.departure_time == pd.Timestamp("1990-01-01 08:10:00")
    assert best_cost == 25


def test_get_best_connection_exact_departure_time(sample_connections):
    current_time = pd.Timestamp("1990-01-01 08:00:00")
    best_connection, best_cost = get_best_connection(current_time, sample_connections)
    assert best_connection is not None

    assert best_connection.line == "A"
    assert best_connection.departure_time == pd.Timestamp("1990-01-01 08:00:00")
    assert best_cost == 5


def test_get_best_connection_multiple_lines_same_departure_time(sample_connections):
    current_time = pd.Timestamp("1990-01-01 08:10:00")
    best_connection, best_cost = get_best_connection(current_time, sample_connections)
    assert best_connection is not None

    assert best_connection.line == "B"
    assert best_connection.departure_time == pd.Timestamp("1990-01-01 08:10:00")
    assert best_cost == 5


def test_get_best_connection_with_wait_time_calculation(sample_connections):
    current_time = pd.Timestamp("1990-01-01 07:55:00")
    best_connection, best_cost = get_best_connection(current_time, sample_connections)
    assert best_connection is not None

    assert best_connection.line == "A"
    assert best_connection.departure_time == pd.Timestamp("1990-01-01 08:00:00")
    assert best_cost == 10
