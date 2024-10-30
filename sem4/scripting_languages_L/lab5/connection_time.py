from log_to_tuple import log_to_tuple
from get_user_from_log import get_user_from_log
import statistics
from datetime import datetime


def connection_time(logList, all=False):
    logs = {}
    for line in logList:
        line = line.strip()
        log_dict = log_to_tuple(line)._asdict()
        user = get_user_from_log(line)
        process = log_dict["Process"]
        if process not in logs:
            logs[process] = {"user": None, "logs": []}
        if user and user.lower() != "unknown":
            logs[process]["user"] = user
        logs[process]["logs"].append(log_dict)
    times = {user: [] for user in set([list["user"] for list in logs.values()])}
    times["all"] = []
    for process, list in logs.items():
        startTime = datetime.strptime(list["logs"][0]["Time"], "%H:%M:%S")
        endTime = datetime.strptime(list["logs"][-1]["Time"], "%H:%M:%S")
        time = endTime - startTime
        times[list["user"]].append(time.total_seconds())
        times["all"].append(time.total_seconds())
    if all:
        avg = statistics.mean(times["all"])
        standard_deviation = (
            statistics.stdev(times["all"]) if len(times["all"]) > 1 else 0
        )
        return {"avg": avg, "stdev": standard_deviation}
    else:
        times.pop("all")
        times.pop(None)
        for user, list in times.items():
            avg = statistics.mean(list)
            standard_deviation = statistics.stdev(list) if len(list) > 1 else 0
            times[user] = {"avg": avg, "stdev": standard_deviation}
        return times
