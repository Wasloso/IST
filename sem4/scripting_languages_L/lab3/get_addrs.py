from read_log import read_log
from log_to_dict import log_to_dict


def get_addrs(logDict):
    return [key for key in logDict.keys()]


if __name__ == "__main__":
    print(get_addrs(log_to_dict(read_log())))
