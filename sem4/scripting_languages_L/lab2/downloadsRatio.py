import stdManager
from extractLogData import logToDict
import re


def getDownloadsRatio(file):
    desiredExtCount = 0
    otherFilesCount = 0
    pattern = re.compile(r".*\.(gif|jpg|jpeg|xbm)$")

    for line in file:
        try:
            content = logToDict(line)
            if pattern.match(content["path"]) and content["requestType"] == "GET":
                desiredExtCount += 1
            elif content["requestType"] == "GET":
                otherFilesCount += 1
        except:
            continue

    stdManager.writeText(f"Downloads ratio: {desiredExtCount / otherFilesCount}\n")


if __name__ == "__main__":
    getDownloadsRatio(stdManager.getInputFile())
