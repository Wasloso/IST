import re


def get_message_type(log: str):
    types = {
        "authentication failure": "Theft attempt",
        "Failed password": "Failed password",
        "Accepted password": "Login successful",
        "Connection closed": "Connection closed",
        "Received disconnect": "Connection closed",
        "Invalid user": "Invalid username",
        "Did not receive identification string from": "Login failed",
    }
    for type, message in types.items():
        match = re.search(type, log)
        if match:
            return message
    return "Other"


if __name__ == "__main__":
    logs = [
        "Dec 10 11:26:56 LabSZ sshd[27074]: Failed password for invalid user admin from 51.15.203.45 port 55034 ssh2",
        "Dec 10 11:27:00 LabSZ sshd[27082]: Received disconnect from 51.15.203.45: 11: Bye Bye [preauth]",
    ]
    for log in logs:
        print(get_message_type(log))
