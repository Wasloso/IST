import sys
import os
import argparse

def backup():
    parser = argparse.ArgumentParser()
    parser.add_argument("--directory",'-d',required=True)
    parser.add_argument('--backupdir', '-b', required=False)
    args = parser.parse_args()

    if not os.path.exists(args.directory) or not os.path.isdir(args.directory):
        sys.stderr.write("Invalid directory path\n")
        sys.exit(1)

    if args.backupdir:
        if args.backupdir != os.environ['BACKUP_DIR']:
            
