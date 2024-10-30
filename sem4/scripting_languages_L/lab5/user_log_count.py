from get_user_from_log import get_user_from_log
from log_to_tuple import log_to_tuple


def user_log_count(logList):
    logs = {}
    for line in logList:
        line = line.strip()
        user = get_user_from_log(line)
        log_dict = log_to_tuple(line)._asdict()
        process = log_dict["Process"]
        if user and user.lower() != "unknown":
            if not user in logs:
                logs[user] = []
            if not process in logs[user]:
                logs[user].append(process)
    most_logs_user = max(logs, key=lambda x: len(logs[x]))
    most_logs_count = len(logs[most_logs_user])
    least_logs_user = min(logs, key=lambda x: len(logs[x]))
    least_logs_count = len(logs[least_logs_user])
    return {
        "most_logs": {"user": most_logs_user, "count": most_logs_count},
        "least_logs": {"user": least_logs_user, "count": least_logs_count},
    }
