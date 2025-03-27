from enum import Enum
from functools import lru_cache, wraps
from .queues import PriorityQueue, Queue
import random
import pandas as pd
from .astar import astar
from .connection import Connection
from .graph import MyGraph
from .utils import (
    cache_cost_function,
    calculate_running_time,
    calculate_total_cost,
    reconstruct_path,
)
from itertools import permutations


def initialize_path(start: str, to_visit: list[str]) -> list[str]:
    random.shuffle(to_visit)
    return [start] + to_visit + [start]


@cache_cost_function()
def calculate_tabu_cost(
    graph: MyGraph, path: list[str], start_time: pd.Timestamp, opt_changes=False
) -> tuple[float, int, list[Connection]]:
    total_time_cost, total_lines_cost = 0, 0
    current_time: pd.Timestamp = start_time
    found_path: list[Connection] = []
    result_path: list[Connection] = []
    stops_to_visit = set(path[1:-1])
    # TODO: add check if stop was visited
    for i in range(len(path) - 1):
        start_stop = path[i]
        end_stop = path[i + 1]
        found_path = astar(graph, path[i], path[i + 1], current_time, opt_changes)[0]
        result_path += found_path
        time_cost, lines_cost = calculate_total_cost(found_path, current_time)
        total_time_cost += time_cost
        total_lines_cost += lines_cost
        current_time = found_path[-1].arrival_time

    return total_time_cost, total_lines_cost, result_path


def adaptive_sample_neighbours(
    path: list[str], no_improvement_count: int, max_no_improvement: int = 5
) -> list[list[str]]:
    all_neighbours = generate_neighbours_insert_move(path)

    if no_improvement_count > max_no_improvement:
        sample_size = min(10, len(all_neighbours))
    else:
        sample_size = max(4, len(all_neighbours) // 5)

    return (
        random.sample(all_neighbours, sample_size)
        if len(all_neighbours) > sample_size
        else all_neighbours
    )


def generate_neighbours_insert_move(path: list[str]) -> list[list[str]]:
    neighbours = []
    middle = path[1:-1]
    n = len(middle)
    for i in range(n):
        for j in range(n):
            if i != j:
                new_middle = middle.copy()
                node = new_middle.pop(i)
                new_middle.insert(j, node)
                neighbours.append([path[0]] + new_middle + [path[-1]])
    return neighbours


def get_tabu_size(to_visit: list[str]) -> int:
    return max(10, len(to_visit) * 3)


@calculate_running_time
def tabu(
    graph: MyGraph,
    start: str,
    start_time: pd.Timestamp,
    to_visit: list[str],
    opt_changes: bool = False,
    max_iters: int = 10,
    h: int = 5,
):
    current_path = initialize_path(start, to_visit)
    best_path = current_path.copy()
    best_time_cost, best_lines_cost, result_path = calculate_tabu_cost(
        graph, best_path, start_time, opt_changes
    )
    best_cost = best_lines_cost if opt_changes else best_time_cost

    tabu_set: set[tuple[str]] = set[tuple[str]]()
    tabu_size = get_tabu_size(to_visit)
    queue: Queue[tuple[list[str]]] = Queue[tuple[list[str]]]()
    history = Queue([0] * h, maxlen=h)
    no_improvement_count = 0

    for _ in range(max_iters):
        neighbours = adaptive_sample_neighbours(current_path, no_improvement_count)
        print(f"Neighbors to visit: {len(neighbours)}")

        best_neighbor = None
        best_neighbor_cost = float("inf")
        best_neighbor_path: list[Connection] = []
        best_neighbor_time_cost = float("inf")
        best_neighbor_lines_cost = float("inf")

        for neighbor in neighbours:

            time_cost, lines_cost, neighbor_path = calculate_tabu_cost(
                graph, neighbor, start_time, opt_changes
            )
            fitness = lines_cost if opt_changes else time_cost
            modification_count = sum(history.elements)
            aspiration_value = best_cost * 1.05 + 0.1 * (
                len(history.elements) - modification_count
            )
            if tuple(neighbor) in tabu_set and fitness >= aspiration_value:
                continue

            if fitness < best_neighbor_cost:
                best_neighbor = neighbor
                best_neighbor_cost = fitness
                best_neighbor_path = neighbor_path
                best_neighbor_time_cost = time_cost
                best_neighbor_lines_cost = lines_cost

        if best_neighbor is None:
            break

        if best_neighbor_cost < best_cost:
            no_improvement_count = 0
            history.put(1)
            best_path = best_neighbor
            best_cost = best_neighbor_cost
            best_time_cost = best_neighbor_time_cost
            best_lines_cost = best_neighbor_lines_cost
            result_path = best_neighbor_path
        else:
            history.put(0)
            no_improvement_count += 1

        current_path = best_neighbor
        tabu_set.add(tuple(best_neighbor))
        queue.put(tuple(best_neighbor))
        if len(queue.elements) > tabu_size:
            tabu_set.remove(queue.get())

    return result_path, best_time_cost, best_lines_cost
