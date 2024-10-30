import stdManager
from getBiggestFile import getBiggestFile
from countRequests import countRequests
from sumFilesSize import sumFilesSize
from downloadsRatio import getDownloadsRatio


def main():
    inputFile = stdManager.getInputFile()
    for fun in [getBiggestFile, countRequests, sumFilesSize, getDownloadsRatio]:
        fun(inputFile.seek(0))


if __name__ == "__main__":
    main()
