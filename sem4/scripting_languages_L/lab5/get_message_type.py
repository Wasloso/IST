import re
from message_types import MessageType as mt
from test_logs import test_logs


def get_message_type(log: str):
    patternToType: dict[str, mt] = {
        "authentication failure": mt.BREAK_IN_ATTEMPT,
        "POSSIBLE BREAK-IN ATTEMPT!": mt.BREAK_IN_ATTEMPT,
        "Failed password": mt.FAILED_PASSWORD,
        "Accepted password": mt.LOGIN_SUCCESSFUL,
        "Connection closed": mt.CONNECTION_CLOSED,
        "Received disconnect": mt.CONNECTION_CLOSED,
        "Invalid user": mt.INVALID_USERNAME,
        "Did not receive identification string from": mt.LOGIN_FAILED,
        ".*": mt.OTHER,
    }
    for pattern, type in patternToType.items():
        match = re.search(pattern.lower(), log.lower())
        if match:
            return type


if __name__ == "__main__":
    for log in test_logs:
        print(get_message_type(log))
