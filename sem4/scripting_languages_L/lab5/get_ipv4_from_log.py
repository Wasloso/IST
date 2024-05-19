import re
from ipaddress import ip_address
from test_logs import test_logs


def get_ipv4_from_log(log: str):
    pattern = r"[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}"
    match = re.findall(pattern, log)
    for ip in match:
        try:
            ip_address(ip).version == 4
        except:
            match.remove(ip)
    return match


if __name__ == "__main__":
    for log in test_logs:
        print(get_ipv4_from_log(log))
    print(get_ipv4_from_log("255.255.255.255 256.255.255.255 0.0.0.0"))
