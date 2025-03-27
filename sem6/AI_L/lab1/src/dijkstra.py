import pandas as pd
from .queues import PriorityQueue
from .connection import Connection
from .graph import MyGraph
from .utils import (
    calculate_running_time,
    get_best_connection,
    reconstruct_path,
)


@calculate_running_time
def dijkstra(graph: MyGraph, start: str, end: str, start_time: pd.Timestamp) -> list:
    queue: PriorityQueue[str] = PriorityQueue[str]()
    queue.put(start, 0)
    path: dict[str, tuple[str, Connection]] = {start: None}
    total_cost: dict[str, int] = {start: 0}
    arrival_times = {start: start_time}
    connection: Connection = None

    while not queue.empty():
        current = queue.get()

        if current == end:
            break

        current_arrival_time = arrival_times[current]
        for neighbour in graph.neighbors(current):

            connection, score, _, _ = graph.find_best_connection(
                current,
                neighbour,
                current_arrival_time,
            )

            if connection is None:
                continue

            tentative_g_score = total_cost[current] + score

            if neighbour not in total_cost or tentative_g_score < total_cost[neighbour]:
                total_cost[neighbour] = tentative_g_score
                path[neighbour] = (current, connection)
                arrival_times[neighbour] = connection.arrival_time
                queue.put(neighbour, tentative_g_score)

    graph.reset_visited()

    return reconstruct_path(path, start, end) if end in path else []
