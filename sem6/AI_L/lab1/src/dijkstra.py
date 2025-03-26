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

        for neighbour in graph.neighbors(current):
            current_arrival_time = arrival_times[current]

            connections = (
                data["data"]
                for data in graph.get_edge_data(current, neighbour).values()
            )
            connection, cost = get_best_connection(current_arrival_time, connections)

            if connection is None:
                continue

            new_cost = total_cost[current] + cost

            if neighbour not in total_cost or new_cost < total_cost[neighbour]:
                total_cost[neighbour] = new_cost
                path[neighbour] = (current, connection)
                arrival_times[neighbour] = connection.arrival_time
                queue.put(neighbour, new_cost)

    if end not in path:
        return []

    return reconstruct_path(path, start, end)
