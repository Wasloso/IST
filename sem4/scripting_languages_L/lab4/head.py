import sys
import os
import argparse


def head():

    parser = argparse.ArgumentParser()
    parser.add_argument("--bytes")
    parser.add_argument("-v")
    parser.add_argument("--file")
    args = parser.parse_args()
    _bytes = 10 if args.bytes is None else int(args.bytes)

    try:
        if args.file is not None:
            file = open(f"{args.file}", "r")
        else:
            file = sys.stdin

        line = file.readline()
        while line and _bytes != 0:
            sys.stdout.write(f"{line}")
            line = file.readline()
            _bytes -= 1
    except:
        raise FileNotFoundError


if __name__ == "__main__":

    head()
