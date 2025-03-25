from functools import cached_property
from datetime import datetime


class Connection:
    def __init__(
        self,
        company: str,
        line: str,
        departure_time: datetime,
        arrival_time: datetime,
        start=None,
        stop=None,
    ):
        self.company: str = company
        self.line: str = line
        self.departure_time: datetime = departure_time
        self.arrival_time: datetime = arrival_time
        self.start_name = start
        self.stop_name = stop

    @cached_property
    def travel_time(self):
        from .utils import calculate_wait_time

        return calculate_wait_time(self.departure_time, self.arrival_time)

    def __repr__(self):
        start_time = self.departure_time.time()
        end_time = self.arrival_time.time()
        return f"({self.line}) {start_time} {self.start_name} -> {end_time} {self.stop_name}"
