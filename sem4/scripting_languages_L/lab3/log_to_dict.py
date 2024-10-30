from entry_to_dict import entry_to_dict
from read_log import read_log


def log_to_dict(tupleList):
    result = {}
    for entry in tupleList:
        result[entry[0]] = result.get(entry[0], [])
        result[entry[0]].append(entry_to_dict(entry))
    return result


if __name__ == "__main__":
    d = log_to_dict(read_log())
    for k, v in d.items():
        print(f"{k}: {v}")
