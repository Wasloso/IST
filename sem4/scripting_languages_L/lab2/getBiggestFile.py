import sys
import stdManager
from extractLogData import logToDict


def getBiggestFile(file):
    path: str
    size: int = 0

    for line in file:
        try:
            content = logToDict(line)
            if content["bytes"] > size:
                path = content["path"]
                size = content["bytes"]
        except:
            continue

    stdManager.writeText(f"Biggest file: {path=}, {size=} bytes\n")


if __name__ == "__main__":
    getBiggestFile(stdManager.getInputFile())
