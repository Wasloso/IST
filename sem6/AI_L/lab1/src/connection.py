from functools import cached_property
from datetime import datetime
from dataclasses import dataclass


@dataclass
class Connection:
    company: str
    line: str
    departure_time: datetime
    arrival_time: datetime
    start: str | None = None
    stop: str | None = None

    @cached_property
    def travel_time(self):
        from .utils import calculate_wait_time

        return calculate_wait_time(self.departure_time, self.arrival_time)

    def __repr__(self):
        start_time = self.departure_time.time()
        end_time = self.arrival_time.time()
        return f"({self.line}) {start_time} {self.start} -> {end_time} {self.stop}"
