import sys
import datetime


def print_dict_entry_dates(logDict, code):
    for host, data in logDict.items():
        firstRequest = min([date["date"] for date in data])
        lastRequest = max([date["date"] for date in data])
        codeRequest = len([d["requestCode"] for d in data if d["requestCode"] == code])
        allRequests = len(data)
        sys.stdout.write(
            f"host: {host}\nfirst request: {firstRequest.date()}\nlast request: {lastRequest.date()}\ncode to all ratio: {codeRequest/allRequests:.2f}\n\n"
        )


if __name__ == "__main__":
    from read_log import read_log
    from log_to_dict import log_to_dict

    print_dict_entry_dates(log_to_dict(read_log()), 200)
