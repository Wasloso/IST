import argparse
import os
import sys
from backup_utils import getFullPath
import time
import random
import subprocess


def writer():
    parser = argparse.ArgumentParser(description="Write a file")
    parser.add_argument("--path", "-p", required=True)
    args = parser.parse_args()

    args.path = getFullPath(args.path)

    if not os.path.exists(args.path):
        sys.stdout.write("File does not exist")
        sys.exit(1)

    names = [
        "John",
        "Jane",
        "Michael",
        "Emily",
        "David",
        "Sarah",
        "Daniel",
        "Jessica",
        "Matthew",
        "Amanda",
    ]
    surnames = [
        "Doe",
        "Smith",
        "Johnson",
        "Williams",
        "Brown",
        "Jones",
        "Garcia",
        "Miller",
        "Davis",
        "Rodriguez",
    ]
    phone_numbers = []
    for _ in range(10):
        phone_number = "+48 " + "".join([str(random.randint(0, 9)) for _ in range(9)])
        phone_numbers.append(phone_number)

    with open(args.path, "a") as file:
        for _ in range(10):
            file.write(
                random.choice(names)
                + " "
                + random.choice(surnames)
                + " "
                + random.choice(phone_numbers)
                + "\n"
            )
            file.flush()
            time.sleep(random.random() * 5)


if __name__ == "__main__":
    writer()
