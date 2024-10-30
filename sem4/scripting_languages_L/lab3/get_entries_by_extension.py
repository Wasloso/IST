import re
from read_log import read_log


def get_entries_by_extension(tupleList, ext):
    return [tuple for tuple in tupleList if ext == tuple[4]]


if __name__ == "__main__":
    for tuple in get_entries_by_extension(read_log(), "txt"):
        print(tuple)
# przyjmuje jako parametr listę krotek reprezentującą log,
# ✓ przyjmuje jako parametr ciąg znaków reprezentujący rozszerzenie pliku (np. “jpg”)
# ✓ zwraca wszystkie wpisy zawierające zapytania o zasoby z danym rozszerzeniem.
