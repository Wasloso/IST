import sys
import pandas as pd
from tabulate import tabulate

from .graph import MyGraph
from .connection import Connection


def calculate_wait_time(
    arrival_time: pd.Timestamp, departure_time: pd.Timestamp
) -> float:
    delta = (departure_time - arrival_time).total_seconds() / 60
    return delta if delta >= 0 else delta + 1440


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
    node, result_path = end, []

    while (node is not None) and (path[node] is not None):
        prev_node, connection = path[node]
        result_path.append(connection)
        node = prev_node

    return result_path[::-1]


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


def cache_cost_function():
    cost_cache = {}

    def decorator(func):
        @wraps(func)
        def wrapper(graph, path, start_time, opt_changes=False):
            key = (tuple(path), start_time, opt_changes)
            if key in cost_cache:
                print("Cache hit ", cost_cache[key])
                return cost_cache[key]
            result = func(graph, path, start_time, opt_changes)
            cost_cache[key] = result
            return result

        return wrapper

    return decorator


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
                connection.start,
                connection.arrival_time.time(),
                connection.stop,
            ]
        )

    headers = ["Line", "Departure Time", "From Stop", "Arrival Time", "To Stop"]

    sys.stdout.write(tabulate(rows, headers=headers, tablefmt="pretty"))

    summary_rows = [
        ["Time cost (minutes)", f"{time_cost:.1f}"],
        ["Lines cost (line changes)", lines_cost],
        ["Algorithm running time (seconds)", f"{total_running_time:.3f}"],
    ]

    sys.stderr.write("\nSummary:\n")
    sys.stderr.write(tabulate(summary_rows, tablefmt="pretty"))
    sys.stderr.write("\n")


def validate_input(
    graph: MyGraph,
    start: str = None,
    end: str = None,
    algorithm: str = None,
    time: str = None,
    optimize: str = None,
):
    if start and end and (start := start.lower()) == (end := end.lower()):
        raise ValueError("Start and end stops must be different")
    if start and (start := start.lower()) not in graph:
        raise ValueError(f"Node {start} not in graph")
    if end and (end := end.lower()) not in graph:
        raise ValueError(f"Node {end} not in graph")
    if algorithm and (algorithm := algorithm.lower()) not in ["d", "a", "t"]:
        raise ValueError(f"Algorithm {algorithm} not in ['d', 'a','t']")
    if optimize and (optimize := optimize.lower()) not in ["t", "p"]:
        raise ValueError(f"Optimize {optimize} not in ['t', 'p']")
    else:
        if optimize == "p":
            optimize = True
        else:
            optimize = False

    if time:
        try:
            time = pd.to_datetime(time, format="%H:%M")
        except ValueError:
            raise ValueError(f"Time {time} not in format HH:MM")

    return start, end, algorithm, time, optimize


def format_input(
    start: str, end: str, algorithm: str, time: str, opt_changes: str
) -> tuple:
    start = start.lower()
    end = end.lower()
    algorithm = algorithm.lower()
    time = pd.to_datetime(time, format="%H:%M")
    opt_changes = True if opt_changes.lower() == "p" else False
    return start, end, algorithm, time, opt_changes
