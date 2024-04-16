import re


def get_user_from_log(log: str):
    pattern = r"user (\S+)"
    match = re.search(pattern, log)
    return match.group(1) if match else None


if __name__ == "__main__":
    print(
        get_user_from_log(
            "Dec 10 09:12:43 LabSZ sshd[24501]: pam_unix(sshd:auth): check pass; user unknown"
        )
    )
