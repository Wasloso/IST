# zadanie 1
def odwr(text):
    return " ".join(text.split()[::-1])


def most_common_element(list):
    return max(set(list), key=list.count)


def sqrt(n, e):
    def helper(n, x):
        return x if abs(x * x - n) < e else helper(n, (x + n / x) / 2)

    return helper(n, n) if n >= 0 else None


def make_alpha_dict(text):
    return {
        k.lower(): [v for v in text.split() if k.lower() in v.lower()]
        for k in text
        if k.isalpha()
    }


def flatten(list):
    match list:
        case []:
            return []
        case [head, *tail]:
            return flatten(head) + flatten(tail)
        case _:
            return [list]


# zadanie 2
def forall(pred, iterable):
    match iterable:
        case []:
            return True
        case [h, *tail]:
            return forall(pred, tail) if pred(h) else False


def exists(pred, iterable):
    match iterable:
        case []:
            return False
        case [h, *tail]:
            return True if pred(h) else exists(pred, tail)


def atleast(n, pred, iterable):
    def helper(iterable, count):
        match iterable:
            case []:
                return count >= n
            case [h, *t]:
                return True if count >= n else helper(t, count + (1 if pred(h) else 0))

    return helper(iterable, 0) if n >= 0 else True


def atmost(n, pred, iterable):
    def helper(iterable, count):
        match iterable:
            case []:
                return count <= n
            case [h, *t]:
                return False if count > n else helper(t, count + (1 if pred(h) else 0))

    return helper(iterable, 0) if n >= 0 else False


if __name__ == "__main__":
    text1 = "Nie wykorzystując imperatywnych instrukcji for, while, if, opracuj implementację każdej z poniższych funkcji"
    text2 = "Ala ma kota"
    text3 = "Funkcja odwr, która przyjmuje na wejściu zdanie jako ciąg znaków i zwraca zdanie z odwróconą kolejnością słów."

    print(f"{odwr(text1)}\n")
    print(f"{odwr(text2)}\n")
    print(f"{odwr(text3)}\n")

    print(f"{most_common_element([1, 2, 3, 4, 5, 1, 1, 1])=}")
    print(f'{most_common_element(["a", "b", "c", "d", "a", "a", "a"])=}')
    print(f"{most_common_element([1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5])=}")

    print(f"{sqrt(4, 0.01)=}")
    print(f"{sqrt(16, 0.001)=}")
    print(f"{sqrt(225, 0.00001)=}")
    print(f"{sqrt(3, 0.1)=}\n")

    print(f"{make_alpha_dict(text1)}\n")
    print(f"{make_alpha_dict(text2)}\n")
    print(f"{make_alpha_dict(text3)}\n")

    print(f"{flatten([[1,2,3],[4,5,[6,7,[8,9]]]])=}")
    print(f"{flatten([1,2,3,[4,5,[6,7,[8,9]]]])=}")
    print(f"{flatten([1,2,3,[],4,5,[[[6],7],8],9])=}")

    # zadanie 2
    pred1 = lambda x: x < 10
    pred2 = lambda x: x == 1
    iterable1 = [1, 2, 3, 4, 1, 1, 5, 6]
    iterable2 = [1, 2, 1, 3, 4, 12]

    print(f"{forall(pred1,iterable1)=}")
    print(f"{forall(pred2,iterable1)=}")
    print(f"{forall(pred1,iterable2)=}")
    print(f"{forall(pred2,iterable2)=}")

    print(f"{exists(pred2,iterable1)=}")
    print(f"{exists(pred2,iterable2)=}")

    print(f"{atleast(3,pred1,iterable1)=}")
    print(f"{atleast(5,pred2,iterable1)=}")

    print(f"{atmost(2,pred2,iterable1)=}")
    print(f"{atmost(3,pred2,iterable1)=}")
