from read_log import read_log


def entry_to_dict(tuple):
    return {
        "host": tuple[0],
        "date": tuple[1],
        "requestType": tuple[2],
        "path": tuple[3],
        "extension": tuple[4],
        "requestCode": tuple[5],
        "bytes": tuple[6],
    }


if __name__ == "__main__":
    for log in read_log():
        print(entry_to_dict(log))

    # return tuple(host, date, reqType, path, ext, reqCode, bytes)
