from read_log import read_log


def get_entries_addr(tupleList, host):
    return [tuple for tuple in tupleList if tuple[0] == host]


if __name__ == "__main__":
    for tuple in get_entries_addr(read_log(), "unicomp6.unicomp.net"):
        print(tuple)


# ✓ przyjmuje jako parametr listę krotek reprezentującą log,
# ✓ przyjmuje jako parametr ciąg znaków reprezentujący adres IP lub nazwę domenową hosta
# wykonującego żądanie,
# ✓ waliduje podany IP lub nazwę domenową hosta
# ✓ zwraca listę wpisów z danym ip lub nazwą domenową host
