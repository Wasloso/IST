import pandas as pd
from src.tabu import tabu
from src.graph import MyGraph
from src.utils import pretty_print, validate_input


def main():
    graph: MyGraph = MyGraph("connection_graph.csv")

    while True:
        start = input("Enter start stop: ")
        time = input("Enter time [HH:MM]: ")
        opt_changes = input("Optimize time/changes [t,p]: ")
        path = input("Enter stops to visit separated by ';' : ")
        try:
            start, _, _, time, opt_changes = validate_input(
                graph, start=start, time=time, optimize=opt_changes
            )
            to_visit = [stop.strip().lower() for stop in path.split(";")]
            for stop in to_visit:
                if stop not in graph:
                    raise ValueError(f"Node {stop} not in graph")
        except ValueError as e:
            print(e)
            continue

        (path, time_cost, lines_cost), running_time = tabu(
            graph, start, time, to_visit, opt_changes, max_iters=20
        )

        pretty_print(path, time_cost, lines_cost, running_time)


if __name__ == "__main__":
    main()
