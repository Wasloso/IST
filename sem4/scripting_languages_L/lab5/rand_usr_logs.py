from log_to_tuple import log_to_tuple
from get_user_from_log import get_user_from_log
import random


def rand_usr_logs(logList) -> tuple[str, list[str]]:
    logs = {}
    for line in logList:
        line = line.strip()
        user = get_user_from_log(line)
        if user and user.lower() != "unknown":
            if user not in logs:
                logs[user] = []
            logs[user].append(line)
    user = random.choice(list(logs.keys()))
    return (user, random.choices(logs[user], k=random.randint(1, len(logs[user]))))
