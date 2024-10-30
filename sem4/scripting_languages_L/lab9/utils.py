import re
from ipaddress import ip_address
from collections import namedtuple


# rozszerzona funkcjonalnosc wzgledem poprzednich labow
def log_to_tuple(log: str) -> (
    tuple[
        str,
        str,
        str,
        str,
        str,
        str | None,
        str | None,
        str | None,
        str,
    ]
    | None
):
    pattern = r"(?P<mon>\D+) (?P<day>\d+) (?P<time>\d\d:\d\d:\d\d) (?P<host>\w*) \w+\[(?P<process>\d+)\]: (?P<message>.*)"
    logTuple = namedtuple(
        "logTuple",
        ["Month", "Day", "Time", "Host", "Process", "User", "IP", "Port", "Message"],
    )
    match = re.match(pattern, log)
    ip: str | None = None
    user: str | None = None
    port: str | None = None

    if match:
        ipMatch = re.search(r"[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}", log)
        if ipMatch:
            try:
                ip = ipMatch[0] if ip_address(ipMatch[0]).version == 4 else None
            except:
                pass
        userMatch = re.search(r"(user|for)(=| )(?P<user>\S+)", log)
        if userMatch:
            user = (
                userMatch.group("user")
                if userMatch.group("user") != "authentication"
                else None
            )
        portMatch = re.search(r"port (\d+)", log)
        if portMatch:
            port = portMatch[1]
        return logTuple(
            match.group("mon"),
            match.group("day"),
            match.group("time"),
            match.group("host"),
            match.group("process"),
            user,
            ip,
            port,
            match.group("message"),
        )
    else:
        return None
