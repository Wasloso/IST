import sys
import os
from get_message_type import get_message_type
from user_log_count import user_log_count
from message_types import MessageType as mt
from log_to_tuple import log_to_tuple
from get_ipv4_from_log import get_ipv4_from_log
from get_user_from_log import get_user_from_log
from log_to_tuple import log_to_tuple
from rand_usr_logs import rand_usr_logs
from connection_time import connection_time
from get_logger import get_logger
from get_parser import get_parser


def read_logs():
    parser = get_parser()
    args = parser.parse_args()

    args.file = os.path.abspath(os.path.expanduser(args.file))
    if not os.path.exists(args.file):
        sys.stderr.write(f"File {args.file} does not exist\n")
        sys.exit(1)

    logger = get_logger(args.log_level)
    logs = []
    with open(
        args.file,
        "rb",
    ) as f:
        for line in f:
            bytes = len(line)
            line = line.decode("utf-8").strip()
            logs.append(line)
            message_type = get_message_type(line)
            logger.debug(f"Bytes read = {bytes}")
            if hasattr(args, "functions"):
                if args.type:
                    print(f"Message Type: {message_type}")
                if args.user:
                    print(f"User: {get_user_from_log(line)}")
                if args.ipv4:
                    print(f"IPv4: {get_ipv4_from_log(line)}")
                if args.tuple:
                    print(f"Tuple: {log_to_tuple(line)}")
            match message_type:
                case mt.LOGIN_SUCCESSFUL | mt.CONNECTION_CLOSED:
                    logger.info(f"{message_type.value} - {line}")
                case mt.LOGIN_FAILED:
                    logger.warning(f"{message_type.value} - {line}")
                case mt.FAILED_PASSWORD | mt.INVALID_USERNAME:
                    logger.error(f"{message_type.value} - {line}")
                case mt.BREAK_IN_ATTEMPT:
                    logger.critical(f"{message_type.value} - {line}")
                case mt.OTHER:
                    logger.debug(f"{message_type.value} - {line}")

    if hasattr(args, "functions"):
        if args.rand_user_logs:
            rand_logs = rand_usr_logs(logs)
            print(f"Random user logs\n{rand_logs[0]}\n{rand_logs[1]}")
        if args.connection_stats_user:
            print(f"Connection stats per user\n{connection_time(logs)}")
        if args.connection_stats_glob:
            print(f"Global connection stats\n{connection_time(logs, all=True)}")
        if args.login_stat:
            print(f"Most/ least logged users\n{user_log_count(logs)}")
    return logs


if __name__ == "__main__":
    read_logs()
