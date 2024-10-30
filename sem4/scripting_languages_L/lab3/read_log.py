import re
import sys
from datetime import datetime


def read_log():
    """
    return tuple(host, date, reqType, path, ext, reqCode, bytes)
    """
    # net-1-141.eden.com - - [01/Jul/1995:00:00:19 -0400] "GET /shuttle/missions/sts-71/images/KSC-95EC-0916.jpg HTTP/1.0" 200 34029
    pattern = re.compile(
        r'(?P<host>\S+) - - \[(?P<date>\S*) -\d{4}\] "(?P<requestType>\S+) (?P<path>\S+)\.(?P<extension>\S+)[\s*|\s+\S+]\s*\S*" (?P<requestCode>\d{3}) (?P<bytes>\d+|-)'
    )
    list = []
    for line in sys.stdin:
        match = pattern.match(line)
        if match:
            host, date, requestType, path, extension, requestCode, bytes = (
                match.group("host"),
                datetime.strptime(match.group("date"), "%d/%b/%Y:%H:%M:%S"),
                match.group("requestType"),
                match.group("path").join(match.group("extension")),
                match.group("extension"),
                int(match.group("requestCode")),
                int(match.group("bytes")) if match.group("bytes") != "-" else 0,
            )
            list.append((host, date, requestType, path, extension, requestCode, bytes))
    return list


if __name__ == "__main__":
    print(read_log())
