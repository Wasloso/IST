import argparse
import sys
import os
import json
import subprocess
from backup_utils import changeBackupDirEnvVar, getFullPath


def restore():
    parser = argparse.ArgumentParser()
    parser.add_argument("--destination", "-d", required=False, default=os.getcwd())
    parser.add_argument(
        "--backupdir", "-b", required=False, default=os.environ["BACKUP_DIR"]
    )
    args = parser.parse_args()

    changeBackupDirEnvVar(args.backupdir)

    args.backupdir = getFullPath(args.backupdir)
    args.destination = getFullPath(args.destination)

    if not os.path.exists(f"{args.backupdir}\\backup_history.json"):
        sys.stderr.write("No backup history found\n")
        sys.exit(1)

    file = open(f"{args.backupdir}\\backup_history.json", "r+")
    data: dict = json.load(file)
    backupFiles = []
    for list in data.values():
        for dir in list:
            backupFiles.append(dir)
    backupFiles.sort(key=lambda x: x["timestamp"])
    for i in range(len(backupFiles)):
        sys.stdout.write(f"{i}: {backupFiles[i]['backup_directory']}\n")

    try:
        index = int(input("Enter index to restore file: "))
        if index < 0 or index >= len(backupFiles):
            raise ValueError
    except ValueError:
        sys.stderr.write("Invalid index\n")
        sys.exit(1)

    backupFile = backupFiles[index]
    data[f'{backupFile["file_path"]}'].remove(backupFile)
    if not data[f'{backupFile["file_path"]}']:
        data.pop(f'{backupFile["file_path"]}')
    file.truncate(0)
    file.seek(0)
    file.write(json.dumps(data))
    file.close()

    subprocess.run(
        [
            "powershell",
            "Expand-Archive",
            "-Force",
            f"{backupFile['path']}",
            f"{args.destination}",
        ]
    )

    subprocess.run(
        [
            "powershell",
            "rm",
            f"{backupFile['path']}",
        ]
    )


if __name__ == "__main__":
    restore()
