from extractLogData import logToDict
import stdManager


def filterByStatusCode(file, code):
    for line in file:
        try:
            content = logToDict(line)
            if content["code"] == code:
                stdManager.writeText(line)
        except:
            continue


if __name__ == "__main__":
    filterByStatusCode(stdManager.getInputFile(), "501")
