import sys
import stdManager
from extractLogData import logToDict


def sumFilesSize(file) -> float:
    sum = 0
    for line in file:
        try:
            sum += logToDict(line)["bytes"]
        except:
            continue
    sum /= 1024**3

    stdManager.writeText(f"Sum of files size: {sum} GB\n")


if __name__ == "__main__":
    sumFilesSize(stdManager.getInputFile())
