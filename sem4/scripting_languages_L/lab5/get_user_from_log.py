import re
from test_logs import test_logs


def get_user_from_log(log: str):
    pattern = r"user(=| )(\S+)"
    match = re.search(pattern, log)
    return match.group(2) if match else None


if __name__ == "__main__":
    for log in test_logs:
        print(get_user_from_log(log))
