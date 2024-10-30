from read_log import read_log
from get_entries_by_extension import get_entries_by_extension


def print_entries(tupleList, start, end):
    if start < 0 or start >= len(tupleList):
        raise ValueError("Wrong start position")
    if end < start or end >= len(tupleList):
        raise ValueError("Wrong stop position")

    for tuple in tupleList[start:end]:
        print(tuple)


if __name__ == "__main__":
    print_entries(get_entries_by_extension(read_log(), "jpg"), 1, 3)


# Napisz funkcję print_entries, która wypisuje kolejne krotki z listy przekazanej do niej jako
# parametr, zaczynając od N krotki, której N zostanie podany jako parametr
