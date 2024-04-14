import os
import sys
import json


def fileInfo():
    file_path = input()
    if not os.path.exists(file_path):
        sys.stderr.write("File does not exist")
        raise FileNotFoundError
    if not os.path.isfile(file_path):
        sys.stderr.write("Not a file")
        raise ValueError
    if not os.access(file_path, os.R_OK):
        sys.stderr.write("No read permission")
        raise PermissionError
    if not os.path.splitext(file_path)[1] == ".txt":
        sys.stderr.write("Not a txt file")
        raise ValueError

    with open(file_path, "r") as file:
        output = {}
        content = file.read()

        output["path"] = os.path.abspath(os.path.expanduser(file_path))
        output["characters_count"] = len(content.replace("\n", ""))
        output["words_count"] = len(content.replace("\n", " ").split())
        output["lines_count"] = content.count("\n")
        output["most_common_character"] = max(
            set(content.replace("\n", "")), key=content.count
        )
        output["most_common_word"] = max(
            content.replace("\n", " ").split(), key=content.replace("\n", " ").count
        )
        # dodanie listy slow dla kazdego pliku, aby w nastepnym zadaniu poprawnie obliczyc najpopularniejsze slowo i znak
        output["words"] = [word for word in content.replace("\n", " ").split()]
        sys.stdout.write(json.dumps(output))


if __name__ == "__main__":
    fileInfo()
