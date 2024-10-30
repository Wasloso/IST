import json
import subprocess
import sys
import os


def logToDir(dirName: str, timestamp: str, path: str, filePath: str):
    return {
        "backup_directory": dirName,
        "path": path,
        "timestamp": timestamp,
        "file_path": filePath,
    }


def changeBackupDirEnvVar(backupdir: str) -> bool:
    """Requires administrator previlages to change environment variables\n
    Returns True if provided path doesnt match enviromental variable BACKUP_DIR"""

    if os.environ["BACKUP_DIR"] != backupdir:
        try:
            subprocess.run(
                [
                    "powershell",
                    f'[Environment]::SetEnvironmentVariable("BACKUP_DIR", "{backupdir}", "Machine")',
                ]
            )
        except:
            sys.stderr.write(
                "Need administrator previlages to change BACKUP_DIR in env vars\n"
            )
            sys.exit(1)
        return True
    return False


def getFullPath(path):
    return os.path.abspath(os.path.expanduser(path))
