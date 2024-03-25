from extractLogData import logToDict
import stdManager
import re


def filterByCountry(file, country):
    pattern = re.compile(r".*\." + re.escape(country) + r"$")

    for line in file:
        try:
            if pattern.match(logToDict(line)["host"]):
                stdManager.writeText(line)
        except:
            continue


if __name__ == "__main__":
    filterByCountry(stdManager.getInputFile(), "pl")
