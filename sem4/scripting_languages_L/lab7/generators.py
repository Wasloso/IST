from functools import lru_cache
import time


def make_generator(f):
    globals()[f.__name__] = f
    i = 0
    while True:
        yield f(i)
        i = i + 1


def make_generator_mem(f):
    @lru_cache
    def mem_f(n):
        return f(n)

    globals()[f.__name__] = mem_f

    return make_generator(mem_f)


def fib(num):
    if num <= 1:
        return 1
    else:
        return fib(num - 1) + fib(num - 2)


def main():
    gen = make_generator(fib)
    start = time.perf_counter()
    for i in range(35):
        print(next(gen))
    print(f"Fib generator time for 35 elements: {time.perf_counter() - start} s")
    gen_mem = make_generator_mem(fib)
    start = time.perf_counter()
    for i in range(35):
        print(next(gen_mem))
    print(
        f"Memoized fib generator time for 35 elements: {time.perf_counter() - start} s"
    )

    f_arithmetic = lambda n: 2 + 3 * n
    f_geomethric = lambda n: 3 * 2**n

    for f in zip(
        [f_arithmetic, f_geomethric],
        ["arithmetic", "geomethric"],
    ):
        print(f"Function: {f[1]}")
        gen = make_generator(f[0])
        print([next(gen) for i in range(15)])


if __name__ == "__main__":
    main()
