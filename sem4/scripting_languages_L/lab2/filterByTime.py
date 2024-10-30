from extractLogData import logToDict
import stdManager
import re


def filterByTime(file, hourStart, hourEnd):
    pattern = re.compile(f"../.../....:(..).*")

    for line in file:
        try:
            hour = int(pattern.match(logToDict(line)["time"]).group(1))
            if hourStart <= hour < hourEnd:
                stdManager.writeText(line)
            elif hourStart > hourEnd:
                if hourStart <= hour or hour < hourEnd:
                    stdManager.writeText(line)
        except:
            continue


if __name__ == "__main__":
    filterByTime(stdManager.getInputFile(), 0, 1)
