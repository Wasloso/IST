from functools import lru_cache
import pandas as pd
from concurrent.futures import ThreadPoolExecutor
import math

from .constants import EARTH_RADIUS
from .queues import PriorityQueue
from .connection import Connection
from .graph import MyGraph
from .utils import (
    calculate_running_time,
    get_best_connection,
    reconstruct_path,
)
from math import radians, sin, cos, sqrt, atan2


@lru_cache(maxsize=None)
def heuristic(
    pos_start: tuple[float, float], pos_end: tuple[float, float], travel_speed=10
) -> float:
    lat1, lon1, lat2, lon2 = map(radians, [*pos_start, *pos_end])
    dlat, dlon = lat2 - lat1, lon2 - lon1
    a = sin(dlat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(dlon / 2) ** 2
    distance = EARTH_RADIUS * 2 * atan2(sqrt(a), sqrt(1 - a))
    return (distance / travel_speed) * 60


def process_neighbor(args):
    (
        neighbour,
        edges,
        current_arrival_time,
        current_line,
        line_change_cost,
        opt_changes,
    ) = args
    connections = [data["data"] for data in edges.values()]
    connection, cost = get_best_connection(
        current_arrival_time,
        connections,
        current_line,
        line_change_cost,
        opt_changes,
    )
    return neighbour, connection, cost


@calculate_running_time
def astar(
    graph: MyGraph,
    start: str,
    end: str,
    start_time: pd.Timestamp,
    opt_changes: bool = False,
    line_change_cost: int = 250,
    max_workers: int = 4,
) -> list[Connection]:
    if start == end:
        return []

    queue = PriorityQueue[str]()
    queue.put(start, 0)

    path = {start: None}
    g_score = {start: 0}
    arrival_times = {start: start_time}
    lines = {start: None}

    end_pos = graph.nodes[end]["data"]

    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        while not queue.empty():
            current = queue.get()

            if current == end:
                break

            current_line = lines[current]
            current_arrival_time = arrival_times[current]
            edge_data = graph[current]

            args = [
                (
                    neighbour,
                    edges,
                    current_arrival_time,
                    current_line,
                    line_change_cost,
                    opt_changes,
                )
                for neighbour, edges in edge_data.items()
            ]

            results = list(executor.map(process_neighbor, args))

            for neighbour, connection, cost in results:
                if connection is None:
                    continue

                tentative_g_score = g_score[current] + cost

                if neighbour not in g_score or tentative_g_score < g_score[neighbour]:
                    path[neighbour] = (current, connection)
                    arrival_times[neighbour] = connection.arrival_time
                    lines[neighbour] = connection.line
                    g_score[neighbour] = tentative_g_score

                    current_pos = graph.nodes[current]["data"]
                    f_score = tentative_g_score + heuristic(current_pos, end_pos)

                    queue.put(neighbour, f_score)

    return reconstruct_path(path, start, end) if end in path else []
