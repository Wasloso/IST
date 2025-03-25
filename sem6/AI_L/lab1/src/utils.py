import pandas as pd
from tabulate import tabulate
from .connection import Connection


def calculate_wait_time(
    arrival_time: pd.Timestamp, departure_time: pd.Timestamp
) -> float:
    if departure_time < arrival_time:
        departure_time += pd.Timedelta(days=1)

    delta = (departure_time - arrival_time).total_seconds() / 60
    return delta


def get_optimal_connection_by_time(
    current_time: pd.Timestamp, connections: dict[int, dict[Connection, int]]
) -> tuple:
    best_connection: Connection = None
    best_cost = float("inf")

    for _, edge in connections.items():
        connection: Connection = edge["data"]
        wait_time = calculate_wait_time(current_time, connection.departure_time)

        if wait_time < best_cost:
            best_connection = connection
            best_cost = wait_time

    return best_connection, best_connection.travel_time


def get_best_connection(
    current_time: pd.Timestamp,
    connections: list[Connection],
    line: str = None,
    line_change_cost: int = 250,
    opt_changes: bool = False,
) -> tuple[Connection, float]:
    best_connection: Connection = None
    best_cost = float("inf")

    for connection in connections:
        cost = (
            calculate_wait_time(current_time, connection.departure_time)
            + connection.travel_time
        )
        if opt_changes and connection.line != line:
            cost += line_change_cost
        if cost < best_cost:
            best_connection = connection
            best_cost = cost
    return best_connection, best_cost


def reconstruct_path(
    path: dict[str, tuple[str, Connection]], start: str, end: str
) -> list[Connection]:
    node = end
    result_path = []
    lines: set = {}
    while node is not None and path[node] is not None:
        prev_node, connection = path[node]
        result_path.append(connection)
        node = prev_node
    result_path.reverse()
    return result_path


def calculate_total_cost(
    path: list[Connection], start_time: pd.Timestamp
) -> tuple[float, int]:
    time_cost = calculate_wait_time(start_time, path[-1].arrival_time)
    lines: set = {connection.line for connection in path}
    return (time_cost, len(lines))


import time
from functools import wraps


def calculate_running_time(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        return result, end - start

    return wrapper


def pretty_print(
    connections: list[Connection],
    time_cost: float,
    lines_cost: int,
    total_running_time: float,
):
    rows = []
    prev_number = None
    for connection in connections:
        line_to_display = "" if connection.line == prev_number else connection.line
        prev_number = connection.line
        rows.append(
            [
                line_to_display,
                connection.departure_time.time(),
                connection.start_name,
                connection.arrival_time.time(),
                connection.stop_name,
            ]
        )
    print("\nSummary:")
    summary_rows = [
        ["Time cost (minutes)", f"{time_cost:.1f}"],
        ["Lines cost (line changes)", lines_cost],
        ["Algorithm running time (seconds)", f"{total_running_time:.3f}"],
    ]
    print(tabulate(summary_rows, tablefmt="pretty"))

    headers = ["Line", "Departure Time", "From Stop", "Arrival Time", "To Stop"]
    print(tabulate(rows, headers=headers, tablefmt="pretty"))
