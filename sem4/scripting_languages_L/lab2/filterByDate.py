from extractLogData import logToDict
import stdManager
import re


def filterByDate(file, date):
    pattern = re.compile(f"(../.../....).*")

    for line in file:
        try:
            content = logToDict(line)
            match = pattern.match(content["time"])
            if match.group(1) == date:
                stdManager.writeText(line)
        except:
            continue
    return


if __name__ == "__main__":
    filterByDate(stdManager.getInputFile(), "26/Jul/1995")
