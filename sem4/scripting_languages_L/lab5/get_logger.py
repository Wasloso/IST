import logging
import sys


def get_logger(level="DEBUG"):
    logger = logging.getLogger()
    logging.basicConfig(
        format="%(levelname)s: %(message)s",
        level=level,
        filemode="w",
        filename="logs.log",
    )
    formatter = logging.Formatter("%(levelname)s: %(message)s")
    stdout_handler = logging.StreamHandler(sys.stdout)
    stdout_handler.setLevel(logging.DEBUG)
    stdout_handler.addFilter(lambda record: record.levelno <= logging.INFO)
    stdout_handler.setFormatter(formatter)
    logger.addHandler(stdout_handler)
    stderr_handler = logging.StreamHandler(sys.stderr)
    stderr_handler.setLevel(logging.ERROR)
    stderr_handler.setFormatter(formatter)
    logger.addHandler(stderr_handler)

    return logger


if __name__ == "__main__":
    pass
