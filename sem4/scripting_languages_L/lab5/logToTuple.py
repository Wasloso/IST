from collections import namedtuple
import re

# Dec 10 06:55:46 LabSZ sshd[24200]: reverse mapping checking getaddrinfo for ns.marryaldkfaczcz.com [173.234.31.186] failed - POSSIBLE BREAK-IN ATTEMPT!
# pattern = r'(\S+) - - \[(\d{2}/\w{3}/\d{4}:\d{2}:\d{2}:\d{2} -\d{4})\] "(\S+) (\S+)(\s{0}|\s+\S+)\s*\S*" (\d{3}) (\d+|-)'


def logToTuple(log: str):
    expr = r"(?P<mon>\D+) (?P<day>\d+) (?P<time>\d\d:\d\d:\d\d) (?P<host>\w*) (?P<process>\w+\[\d+\]): (?P<message>.*)"
    match = re.match(expr, log)
    if match:
        mon, day, time, host, process, message = (
            match.group("mon"),
            match.group("day"),
            match.group("time"),
            match.group("host"),
            match.group("process"),
            match.group("message"),
        )
        logTuple = namedtuple(
            "Log", ["Month", "Day", "Time", "Host", "Process", "Message"]
        )
        return logTuple(mon, day, time, host, process, message)


if __name__ == "__main__":
    pass
