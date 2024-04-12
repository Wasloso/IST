import sys
import os
import argparse
import time


def head():

    parser = argparse.ArgumentParser()
    parser.add_argument("--bytes", "-b", type=int, default=10)
    parser.add_argument("-v", action="store_true")
    parser.add_argument("--file", "-f", type=str)
    parser.add_argument("--follow", action="store_true")
    args = parser.parse_args()

    try:
        # brak mozliwosci odczytania nazwy pliku jesli jest przekazywan przez potokowanie
        if args.v and args.file:
            sys.stdout.write(f"File: {args.file}\n")

        if args.file:
            file = open(args.file, "rt")
        else:
            file = sys.stdin

        for _ in range(args.bytes):
            sys.stdout.write(file.readline())

        if args.follow:
            file.seek(0, os.SEEK_END)
            timePassed = 0
            while timePassed <= 5:
                line = file.readline()
                if not line:
                    time.sleep(0.1)
                    timePassed += 0.1
                    continue
                timePassed = 0
                sys.stdout.write(line)

        file.close()

    except FileNotFoundError:
        sys.stderr.write("File not found\n")
    except Exception as e:
        sys.stderr.write(f"Something went wrong: {str(e)}\n")


if __name__ == "__main__":
    head()
