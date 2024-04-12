import argparse
import sys
import os


def showPath():
    parser = argparse.ArgumentParser()
    parser.add_argument("--detailed", "-d", action="store_true")
    args = parser.parse_args()

    path = os.environ["PATH"]
    dirs = path.split(os.pathsep)

    for dir in dirs:
        sys.stdout.write(f"{dir}")
        if args.detailed:
            list = []
            for file in os.listdir(dir):
                if os.access(os.path.join(dir, file), os.X_OK):
                    list.append(file)

            sys.stdout.write(f": {list}")
        sys.stdout.write("\n")


if __name__ == "__main__":
    showPath()
