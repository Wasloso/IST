import inspect
import logging
import time
from datetime import datetime


def log(LOG_LEVEL=logging.DEBUG):
    def decorator(instance):
        def wrapper(*args, **kwargs):
            logger = logging.getLogger()
            logger.setLevel(LOG_LEVEL)
            format = logging.Formatter("%(levelname)s : %(message)s")
            if not logger.handlers:
                console_handler = logging.StreamHandler()
                console_handler.setFormatter(format)
                logger.addHandler(console_handler)

            if inspect.isclass(instance):
                logger.log(LOG_LEVEL, f"Class {instance.__name__} instance created")
                return instance(*args, **kwargs)
            else:
                logger.log(
                    LOG_LEVEL,
                    f"Called function {instance.__name__} at {datetime.now()}",
                )
                logger.log(LOG_LEVEL, f"Arguments: {args}, {kwargs}")
                start = time.perf_counter_ns()
                result = instance(*args, **kwargs)
                end = time.perf_counter_ns()
                logger.log(LOG_LEVEL, f"Execution time: {end - start} ns")
                logger.log(LOG_LEVEL, f"Returned: {result}")
                return result

        return wrapper

    return decorator


if __name__ == "__main__":

    @log()
    def fun1(a, b, c):
        return a + b + c

    @log(LOG_LEVEL=logging.INFO)
    def fun2(a, b=11, c=10):
        return a + b + c

    @log(LOG_LEVEL=logging.WARNING)
    class A:
        def __init__(self):
            pass

    @log(LOG_LEVEL=logging.CRITICAL)
    class B:
        def __init__(self):
            pass

    fun1(1, 2, 3)
    fun2(1, b=2, c=10)
    a = A()
    b = B()
