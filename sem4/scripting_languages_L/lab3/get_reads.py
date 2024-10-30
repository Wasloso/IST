from read_log import read_log


def get_reads(tupleList, http1, http2, connected=False):
    list1 = [tuple for tuple in tupleList if (tuple[4] == http1)]
    list2 = [tuple for tuple in tupleList if (tuple[4] == http2)]
    if connected:
        return [*list1, *list2]
    return list1, list2


if __name__ == "__main__":
    for list in get_reads(read_log(), 404, 304):
        print(list)
# przyjmuje jako parametr listę krotek reprezentującą log
# ✓ przyjmuje jako 2 parametry kody statusu HTTP
# ✓ przyjmuje opcjonalny parametr logiczny, który określa, czy zwrócić jedną, połączoną listę czy
# dwie osobne,
# ✓ tworzy dwie oddzielne listy zawierające wpisy z kodami statusu HTTP
# ✓ zwraca odpowiedni wynik
