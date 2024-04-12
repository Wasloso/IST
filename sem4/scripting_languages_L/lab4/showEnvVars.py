import sys
import os


def show_env_vars(option=1):
    vars = sorted(os.environ.items())
    paramVars = sys.argv[1:]

    if paramVars:
        sys.stdout.write(
            f"Filtered variables: {[key for key,value in vars if key in paramVars]}"
        )
        return

    sys.stdout.write(f"Env variables: {[key for key, value in vars]}\n")


if __name__ == "__main__":
    show_env_vars()
