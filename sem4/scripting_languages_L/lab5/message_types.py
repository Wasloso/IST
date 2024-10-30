from enum import Enum


class MessageType(Enum):
    # type = description
    BREAK_IN_ATTEMPT = "Break in attempt"
    FAILED_PASSWORD = "Failed password"
    LOGIN_SUCCESSFUL = "Accepted password"
    CONNECTION_CLOSED = "Connection closed"
    INVALID_USERNAME = "Invalid username"
    LOGIN_FAILED = "Login failed"
    OTHER = "Other"

    def __str__(self):
        return self.value
