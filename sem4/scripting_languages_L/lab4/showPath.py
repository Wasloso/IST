import sys
import os


def showPath(option=1):
    path = os.environ["PATH"]
    dirs = path.split(os.pathsep)

    ex = sys.argv[1]

    if ex not in ["1", "2"]:
        raise Exception("No such option")

    for dir in dirs:
        sys.stdout.write(f"{dir}")
        if ex == "2":
            list = []
            try:
                for file in os.listdir(dir):
                    if os.access(os.path.join(dir, file), os.X_OK):
                        list.append(file)
            except:
                pass
            sys.stdout.write(f": {list}")
        sys.stdout.write("\n")


if __name__ == "__main__":
    showPath()
