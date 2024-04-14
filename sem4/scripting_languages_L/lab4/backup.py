import os
import argparse
import subprocess
from datetime import datetime
from backup_utils import logToDir, changeBackupDirEnvVar, getFullPath
import json
import json
import sys


def backup():
    parser = argparse.ArgumentParser()
    parser.add_argument("--directory", "-d", default=os.getcwd())
    parser.add_argument(
        "--backupdir", "-b", required=False, default=os.environ["BACKUP_DIR"]
    )
    args = parser.parse_args()

    args.backupdir = getFullPath(args.backupdir)
    args.directory = getFullPath(args.directory)

    if not os.path.exists(args.directory) or not os.path.isdir(args.directory):
        sys.stderr.write("Invalid directory path\n")
        sys.exit(1)

    currentBackupDir = os.environ["BACKUP_DIR"]
    backupDirChange = False
    if changeBackupDirEnvVar(args.backupdir):
        backupDirChange = True
        if not os.path.exists(args.backupdir):
            subprocess.run(["powershell", "mkdir", args.backupdir])
        subprocess.run(
            ["powershell", "cp", "-r", f"{currentBackupDir}\\*", args.backupdir]
        )

    timestamp = datetime.now().strftime("%Y-%m-%d-%H-%M-%S")
    dirname = args.directory.split("\\")[-1]
    ext = ".zip"
    backupfile = f"{timestamp}-{dirname}{ext}"

    subprocess.run(
        [
            "powershell",
            "Compress-Archive",
            f"{args.directory}\\*",
            f"{args.backupdir}\\{backupfile}",
        ]
    )

    if not os.path.exists(f"{args.backupdir}\\backup_history.json"):
        file = open(f"{args.backupdir}\\backup_history.json", "w+")
        file.write("{}")
        file.close()

    file = open(f"{args.backupdir}\\backup_history.json", "r+")
    content = file.read()
    if not content:
        file.write("{}")
    file.seek(0)
    data: dict = json.load(file)
    if not data.get(args.directory):
        data[args.directory] = []
    data.get(args.directory).append(
        logToDir(
            backupfile, timestamp, f"{args.backupdir}\\{backupfile}", args.directory
        )
    )

    # w przypadku zmiany ścieżki backupu, zmieniamy ścieżki w pliku historii
    if backupDirChange:
        for k, v in data.items():
            for log in v:
                log["path"] = log["path"].replace(currentBackupDir, args.backupdir)

    file.seek(0)
    file.write(json.dumps(data))
    file.close()


if __name__ == "__main__":
    backup()
