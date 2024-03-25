import re


def logToDict(logLine) -> dict:
    """keys: host, time, requestType, path, code, bytes"""
    pattern = r'(\S+) - - \[(\d{2}/\w{3}/\d{4}:\d{2}:\d{2}:\d{2} -\d{4})\] "(\S+) (\S+)(\s{0}|\s+\S+)\s*\S*" (\d{3}) (\d+|-)'
    match = re.match(pattern, logLine)
    if match:
        host = match.group(1)
        time = match.group(2)
        requestType = match.group(3)
        path = match.group(4)
        code = match.group(6)
        bytes = match.group(7)
        return {
            "host": host,
            "time": time,
            "requestType": requestType,
            "path": path,
            "code": code,
            "bytes": int(bytes) if bytes != "-" else 0,
        }
    else:
        raise ValueError("Invalid format")


# test
if __name__ == "__main__":
    print(
        logToDict(
            'ux10.hrz.uni-dortmund.de - - [01/Jul/1995:07:14:22 -0400] "GET /images/ksclogosmall.gif HTTP/1.0" 200 3635'
        )
    )
    print(
        logToDict(
            '133.54.246.57 - - [01/Jul/1995:07:13:09 -0400] "GET /shuttle/missions/sts-71/sts-71-patch.jpg HTTP/1.0" 200 203429  '
        )
    )
