from collections import namedtuple
import re
from test_logs import test_logs

# Dec 10 06:55:46 LabSZ sshd[24200]: reverse mapping checking getaddrinfo for ns.marryaldkfaczcz.com [173.234.31.186] failed - POSSIBLE BREAK-IN ATTEMPT!


def log_to_tuple(log: str) -> namedtuple:
    pattern = r"(?P<mon>\D+) (?P<day>\d+) (?P<time>\d\d:\d\d:\d\d) (?P<host>\w*) \w+\[(?P<process>\d+)\]: (?P<message>.*)"
    logTuple = namedtuple("Log", ["Month", "Day", "Time", "Host", "Process", "Message"])
    match = re.match(pattern, log)
    if match:
        return logTuple(
            match.group("mon"),
            match.group("day"),
            match.group("time"),
            match.group("host"),
            match.group("process"),
            match.group("message"),
        )


if __name__ == "__main__":
    for log in test_logs:
        print(log_to_tuple(log))
