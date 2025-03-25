import pandas as pd
from .priority_queue import PriorityQueue
from .connection import Connection
from .graph import MyGraph
from .utils import (
    calculate_running_time,
    get_best_connection,
    reconstruct_path,
)
from math import radians, sin, cos, sqrt, atan2


def heuristic(
    pos_start: tuple[float, float],
    pos_end: tuple[float, float],
    travel_speed=10,
) -> pd.Timedelta:

    lat1, lon1, lat2, lon2 = map(radians, [*pos_start, *pos_end])

    dlat = lat2 - lat1
    dlon = lon2 - lon1
    a = sin(dlat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(dlon / 2) ** 2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))
    earth_radius = 6371
    distance = earth_radius * c

    return (distance / travel_speed) * 60


@calculate_running_time
def astar(
    graph: MyGraph,
    start: str,
    end: str,
    start_time: pd.Timestamp,
    opt_changes: bool = True,
    line_change_cost: int = 250,
) -> list:
    queue: PriorityQueue[str] = PriorityQueue[str]()
    queue.put(start, 0)
    path: dict[str, tuple[str, Connection]] = {start: None}
    total_cost: dict[str, int] = {start: 0}
    arrival_times = {start: start_time}
    lines: dict[str, str] = {start: None}
    connection: Connection = None

    while not queue.empty():
        current = queue.get()
        current_line = lines[current]

        if current == end:
            break

        for neighbour in graph.neighbors(current):
            current_arrival_time = arrival_times[current]

            connections = (
                data["data"]
                for data in graph.get_edge_data(current, neighbour).values()
            )

            connection, cost = get_best_connection(
                current_arrival_time,
                connections,
                current_line,
                line_change_cost,
                opt_changes,
            )

            if connection is None:
                continue

            new_cost = total_cost[current] + cost

            if neighbour not in total_cost or new_cost < total_cost[neighbour]:

                pos_start = graph.nodes[current]["data"]
                pos_end = graph.nodes[neighbour]["data"]
                total_cost[neighbour] = new_cost + heuristic(pos_start, pos_end)
                path[neighbour] = (current, connection)
                arrival_times[neighbour] = connection.arrival_time
                lines[neighbour] = connection.line
                queue.put(neighbour, new_cost)

    if end not in path:
        return []

    return reconstruct_path(path, start, end)


if __name__ == "__main__":
    lat1, lat2 = 51.092268, 51.087869
    lon1, lon2 = 16.892523, 16.909206
    travel_time = heuristic((lat1, lon1), (lat2, lon2))
    print(f"Travel time: {travel_time}")
