import pandas as pd
import networkx as nx
from .connection import Connection
from .constants import *


class MyGraph(nx.MultiDiGraph):

    def __init__(self, filename: str, take: int = None):
        super(MyGraph, self).__init__()

        df = pd.read_csv(
            filename,
            converters={DEPARTURE_TIME: lambda x: x.lower(), ARRIVAL_TIME: str},
            low_memory=False,
        )

        df[DEPARTURE_TIME] = df[DEPARTURE_TIME].apply(self.normalize_time)
        df[ARRIVAL_TIME] = df[ARRIVAL_TIME].apply(self.normalize_time)

        df[DEPARTURE_TIME] = pd.to_datetime(df[DEPARTURE_TIME], format="%H:%M:%S")
        df[ARRIVAL_TIME] = pd.to_datetime(df[ARRIVAL_TIME], format="%H:%M:%S")

        nodes = set(self.nodes)

        for i, row in df.iterrows():
            if take is not None and i >= take:
                break

            start_name, end_name = row[START_STOP].lower(), row[END_STOP].lower()

            connection = Connection(
                company=row[COMPANY],
                line=row[LINE],
                departure_time=row[DEPARTURE_TIME],
                arrival_time=row[ARRIVAL_TIME],
                start=start_name,
                stop=end_name,
            )

            if start_name not in nodes:
                self.add_node(
                    start_name, data=(row[START_STOP_LAT], row[START_STOP_LON])
                )
                nodes.add(start_name)

            if end_name not in nodes:
                self.add_node(end_name, data=(row[END_STOP_LAT], row[END_STOP_LON]))
                nodes.add(end_name)

            self.add_edge(
                start_name, end_name, data=connection, weight=connection.travel_time
            )

    def normalize_time(self, time: str) -> str:
        hour, minute, second = map(int, time.split(":"))
        if hour >= 24:
            hour = hour % 24
        return f"{hour:02}:{minute:02}:{second:02}"
