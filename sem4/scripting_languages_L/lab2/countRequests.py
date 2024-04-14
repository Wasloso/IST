import stdManager
from extractLogData import logToDict


def countRequests(file) -> dict[str, int]:
    requests = {}
    for line in file:
        try:
            code = logToDict(line)["code"]
            requests[code] = requests.get(code, 0) + 1
        except:
            continue

    for k, v in requests.items():
        stdManager.writeText(f"Code: {k}, count: {v}\n")


if __name__ == "__main__":
    countRequests(stdManager.getInputFile())
