import pandas as pd

from src.astar import astar
from src.connection import Connection
from src.dijkstra import dijkstra
from src.graph import MyGraph
from src.utils import calculate_total_cost, format_input, pretty_print, validate_input


def main():
    graph: MyGraph = MyGraph("connection_graph.csv")

    while True:
        start = input("Enter start stop: ")
        end = input("Enter end stop: ")
        algorithm = input("Enter algorithm [d,a]: ")
        time = input("Enter time [HH:MM]: ")
        opt_changes = input("Optimize time/changes [t,p]: ")
        try:
            start, end, algorithm, time, opt_changes = validate_input(
                graph, start, end, algorithm, time, opt_changes
            )
        except ValueError as e:
            print(e)
            continue
        connections, total_time = run_algorithm(
            start, end, algorithm, time, opt_changes, graph
        )
        time_cost, lines_cost = calculate_total_cost(connections, time)
        pretty_print(connections, time_cost, lines_cost, total_time)


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
