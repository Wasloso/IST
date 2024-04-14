import sys
import os
import argparse


def showEnvVars():
    parser = argparse.ArgumentParser()
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--all", "-a", action="store_true")
    group.add_argument("--filter", "-f", nargs="+")
    args = parser.parse_args()

    vars = sorted(os.environ.keys())

    if args.all:
        for var in vars:
            sys.stdout.write(f"{var}\n")
    if args.filter:
        for var in vars:
            if var in args.filter:
                sys.stdout.write(f"{var}\n")


if __name__ == "__main__":
    showEnvVars()
