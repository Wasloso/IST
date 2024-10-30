import re
from datetime import datetime


def read_log(file):
    """
    return tuple(host, date, reqType, path, ext, reqCode, bytes)
    """
    # net-1-141.eden.com - - [01/Jul/1995:00:00:19 -0400] "GET /shuttle/missions/sts-71/images/KSC-95EC-0916.jpg HTTP/1.0" 200 34029
    pattern = re.compile(
        r'(?P<host>\S+) - - \[(?P<date>\S{11}):(?P<time>\S*) (?P<timezone>-\d{4})\] "(?P<requestType>\S+) (?P<path>\S+)\.(?P<extension>\S+)[\s*|\s+\S+]\s*\S*" (?P<requestCode>\d{3}) (?P<bytes>\d+|-)'
    )
    list = []
    with open(file, "r", encoding="utf-8") as f:
        content = f.readlines()
        for line in content:
            match = pattern.match(line)
            if match:
                print(match.group("timezone"))
                list.append(
                    {
                        "host": match.group("host"),
                        "date": datetime.strptime(
                            match.group("date"), "%d/%b/%Y"
                        ).date(),
                        "time": datetime.strptime(
                            match.group("time"), "%H:%M:%S"
                        ).time(),
                        "requestType": match.group("requestType"),
                        "path": match.group("path").join(match.group("extension")),
                        "extension": match.group("extension"),
                        "requestCode": int(match.group("requestCode")),
                        "bytes": (
                            int(match.group("bytes"))
                            if match.group("bytes") != "-"
                            else 0
                        ),
                        "timezone": match.group("timezone"),
                        "raw": line,
                    }
                )

    return list


def filterByDate(logs: list[dict], datefrom, dateto):
    return list(filter(lambda x: datefrom <= x["date"] <= dateto, logs))
