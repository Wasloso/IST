import argparse


def get_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--file",
        "-f",
        required=True,
        help='Path to file eg. --file="./SHH.log"',
    )
    parser.add_argument(
        "--log_level",
        "-l",
        type=str,
        default="DEBUG",
        choices=["DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"],
        help="Set the logging level, default is DEBUG",
    )
    subparser = parser.add_subparsers()
    fun_parser = subparser.add_parser(
        "functions", help="Additional infromation to print"
    )
    fun_parser.set_defaults(functions=True)
    fun_parser.add_argument(
        "--ipv4", action="store_true", help="Print IPv4 addresses in logs"
    )
    fun_parser.add_argument("--user", action="store_true", help="Print users in logs")
    fun_parser.add_argument("--type", action="store_true", help="Print message type")
    fun_parser.add_argument("--tuple", action="store_true", help="Print log as tuple")
    fun_parser.add_argument(
        "--rand_user_logs",
        "-rul",
        action="store_true",
        help="Print random user n random logs",
    )
    fun_parser.add_argument(
        "--connection_stats_glob",
        "-sg",
        action="store_true",
        help="Print global connection stats",
    )
    fun_parser.add_argument(
        "--connection_stats_user",
        "-su",
        action="store_true",
        help="Print connection stats per user",
    )
    fun_parser.add_argument(
        "--login_stat", "-ls", action="store_true", help="Print most/least logged users"
    )
    return parser
