from read_log import read_log


def sort_log(tupleList, number):
    if number < 0 or number > 6:
        raise ValueError("Number must be between 0 and 5")

    return sorted(tupleList, key=lambda x: x[number])


if __name__ == "__main__":
    for tuple in sort_log(read_log(), 5):
        print(tuple)
