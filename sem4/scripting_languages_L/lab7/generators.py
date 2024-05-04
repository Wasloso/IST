from functools import cache
import time


def make_generator(f):
    i = 0
    while True:
        i = i + 1
        yield f(i)


def make_generator_mem(f):
    @cache
    def mem_f(f):
        return f

    return make_generator(mem_f)


def fib(num):
    if num <= 1:
        return 1
    else:
        return fib(num - 1) + fib(num - 2)


if __name__ == "__main__":
    gen = make_generator(fib)
    gen_mem = make_generator_mem(fib)

    start = time.perf_counter()
    for _ in range(25):
        next(gen)
    print(f"Time: {time.perf_counter() - start} s")
    start = time.perf_counter()
    for _ in range(25):
        next(gen_mem)
    print(f"Time: {time.perf_counter() - start} s")

    f_arithmetic = lambda n: 2 + 3 * n
    f_geomethric = lambda n: 3 * 2**n
    f_sqrt = lambda n: n**0.5

    for f in zip(
        [f_arithmetic, f_geomethric, f_sqrt], ["arithmetic", "geomethric", "sqrt"]
    ):
        print(f"Function: {f[1]}")
        gen = make_generator(f[0])
        for i in range(10):
            print(f"{i+1}: {next(gen)}")
