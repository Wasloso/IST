import os
import sys
import subprocess
import json


def analyzeFiles():
    dirPath = sys.argv[1]
    files = os.listdir(dirPath)
    output = {}
    list = []

    for file in files:
        details = subprocess.run(
            ["python", "fileInfo.py"],
            input=f"{dirPath}/{file}\n",
            text=True,
            capture_output=True,
        )
        if details.returncode == 0:
            list.append(json.loads(details.stdout))
    output["analyzed_files_count"] = len(list)
    output["total_characters"] = sum([file["characters_count"] for file in list])
    output["total_words"] = sum([file["words_count"] for file in list])
    output["total_lines"] = sum([file["lines_count"] for file in list])
    words = [word for file in list for word in file["words"]]
    output["most_common_character"] = max(
        set("".join(words).replace("\n", "")), key="".join(words).count
    )
    output["most_common_word"] = max(words, key=words.count)
    sys.stdout.write(json.dumps(output))


if __name__ == "__main__":
    analyzeFiles()
