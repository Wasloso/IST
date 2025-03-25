import pandas as pd

from src.astar import astar
from src.connection import Connection
from src.dijkstra import dijkstra
from src.graph import MyGraph
from src.utils import calculate_total_cost, pretty_print


def main():
    graph: MyGraph = MyGraph("connection_graph.csv")

    while True:
        start = input("Enter start stop: ")
        end = input("Enter end stop: ")
        algorithm = input("Enter algorithm [d,a]: ")
        time = input("Enter time [HH:MM]: ")
        opt_changes = input("Optimize time/changes [t,p]: ")
        try:
            validate_input(start, end, algorithm, time, opt_changes, graph)
        except ValueError as e:
            print(e)
            continue
        start, end, algorithm, time, opt_changes = format_input(
            start, end, algorithm, time, opt_changes
        )
        connections, total_time = run_algorithm(
            start, end, algorithm, time, opt_changes, graph
        )
        time_cost, lines_cost = calculate_total_cost(connections, time)
        pretty_print(connections, time_cost, lines_cost, total_time)


def validate_input(
    start: str, end: str, algorithm: str, time: str, optimize: str, graph: MyGraph
):
    if start.lower() == end.lower():
        raise ValueError("Start and end stops must be different")
    if start.lower() not in graph:
        raise ValueError(f"Node {start} not in graph")
    if end.lower() not in graph:
        raise ValueError(f"Node {end} not in graph")
    if algorithm.lower() not in ["d", "a"]:
        raise ValueError(f"Algorithm {algorithm} not in ['d', 'a']")
    if optimize.lower() not in ["t", "p"]:
        raise ValueError(f"Optimize {optimize} not in ['t', 'p']")
    try:
        pd.to_datetime(time, format="%H:%M")
    except ValueError:
        raise ValueError(f"Time {time} not in format HH:MM")


def format_input(
    start: str, end: str, algorithm: str, time: str, opt_changes: str
) -> tuple:
    start = start.lower()
    end = end.lower()
    algorithm = algorithm.lower()
    time = pd.to_datetime(time, format="%H:%M")
    opt_changes = True if opt_changes.lower() == "p" else False
    return start, end, algorithm, time, opt_changes


def run_algorithm(
    start: str,
    end: str,
    algorithm: str,
    time: pd.Timestamp,
    opt_changes: bool,
    graph: MyGraph,
) -> tuple[list[Connection], float]:
    match algorithm:
        case "d":
            return dijkstra(graph, start, end, time)
        case "a":
            return astar(graph, start, end, time, opt_changes)


if __name__ == "__main__":
    main()
