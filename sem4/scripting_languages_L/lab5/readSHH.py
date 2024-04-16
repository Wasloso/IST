import sys
import os
import argparse


def readLogs():
    with open("SSH.log", "r", encoding="utf-8") as f:
        print(f)


if __name__ == "__main__":
    readLogs()
