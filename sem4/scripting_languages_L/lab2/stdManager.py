import sys
from extractLogData import logToDict


def getInputFile():
    try:
        file = sys.stdin
        return file
    except:
        raise FileNotFoundError


def writeTextFileContent(file):
    for line in file:
        sys.stdout.write(line)


def writeText(text):
    sys.stdout.write(text)


if __name__ == "__main__":
    writeTextFileContent(getInputFile())
