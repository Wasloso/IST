import re


def get_ipv4_from_log(log: str):
    pattern = r"[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}.[0-9]{1,3}"
    match = re.findall(pattern, log)
    return match


if __name__ == "__main__":
    print(
        get_ipv4_from_log(
            "Dec 10 06:55:46 LabSZ sshd[24200]: reverse mapping checking getaddrinfo for ns.marryaldkfaczcz.com [173.234.31.186] failed - POSSIBLE BREAK-IN ATTEMPT!"
        )
    )
