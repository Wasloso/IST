from SSHLogEntry import (
    SSHLogEntry,
    LogError,
    LogInvalidPassword,
    LogOther,
    LogPasswordAccepted,
)
from SSHUser import SSHUser
from SSHLogJournal import SSHLogJournal
from datetime import datetime
import itertools


def getEntryList() -> list[SSHLogEntry]:
    return [
        LogInvalidPassword(
            user="webmaster",
            PID="24200",
            ip="172.234.31.186",
            port="38926",
            date=datetime(year=2024, day=10, month=12, hour=6, minute=55, second=48),
            rawLog="Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2",
        ),
        LogInvalidPassword(
            user="webmaster",
            PID="24200",
            ip="172.234.31.186",
            port="38926",
            date=datetime(year=2024, day=10, month=12, hour=6, minute=55, second=48),
            rawLog="Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2",
        ),
        LogPasswordAccepted(
            user="fztu",
            PID="24680",
            ip="119.137.62.142",
            port="49116",
            date=datetime(year=2024, day=11, month=12, hour=9, minute=32, second=20),
            rawLog="Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh2",
        ),
        LogPasswordAccepted(
            user="fztu",
            PID="24680",
            ip="119.137.62.142",
            port="49116",
            date=datetime(year=2024, day=11, month=12, hour=9, minute=32, second=20),
            rawLog="Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.50 port 49116 ssh2",
        ),
        LogError(
            PID="25455",
            ip="103.99.0.122",
            date=datetime(year=2024, day=12, month=12, hour=11, minute=3, second=44),
            rawLog="Dec 12 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 103.99.0.122: 14: No more user authentication methods available. [preauth]",
        ),
        LogOther(
            PID="24680",
            date=datetime(year=2024, day=10, month=12, hour=9, minute=32, second=20),
            rawLog="Dec 10 09:32:20 LabSZ sshd[24680]: no information provided",
        ),
    ]


def getEntryJournal() -> SSHLogJournal:
    journal = SSHLogJournal()
    journal.append(
        "Dec 10 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh"
    )
    journal.append(
        "Dec 10 09:32:20 LabSZ sshd[24680]: Failed password for fztu from 119.137.62.142 port 49116 ssh"
    )
    journal.append(
        "Dec 10 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 103.99.0.122: 14: No more user authentication methods"
    )
    journal.append(
        "Dec 13 11:03:44 LabSZ sshd[25455]: other: Received disconnect from 103.99.0.122: 14: No more user authentication methods"
    )
    journal.append(
        "Dec 12 11:03:44 LabSZ sshd[25455]: Accepted password: user webmaster from 103.99.0.23: 14: No more user authentication methods"
    )
    journal.append(
        "Dec 11 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 103.100.0.122: 14: No more user authentication methods"
    )
    return journal


def getUserList() -> list[SSHUser]:
    return [
        SSHUser("longName", datetime(1990, 12, 3)),
        SSHUser("srtn", datetime(1990, 12, 3)),
        SSHUser("123456", datetime(1990, 12, 3)),
        SSHUser("a_A_a_A", datetime(1990, 12, 3)),
    ]


def testJournal():
    journal = getEntryJournal()
    print(f"{len(journal)=}")
    print(
        f'Log in journal: {"Dec 10 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh" in journal}'
    )
    print(f'Log in journal: {"aaa" in journal}')
    # slice od 2 do 4 (bez 4)
    print(journal[2:4])
    # slice co drugi
    print(journal[::2])

    # wykorzystanie __iter__
    for entry in journal:
        print(f"{entry}")

    # __getattribute__
    print(journal.ip_192_168_0_1)
    print(journal.ip_119_137_62_142)
    print(journal.date_2024_12_10)

    print(journal.filterLogs(user="webmaster"))
    print(journal.filterLogs(ip="119.137.62.142"))


def duckTyping():
    # mimo ze sa to rozne klasy mozemy na nich wywolywac metode validate
    for entry in [*getEntryJournal(), *getUserList()]:
        print(entry.validate())


def zad_1():
    log = LogOther(
        date=datetime(2024, 12, 10, 9, 32, 20),
        rawLog="Dec 10 09:32:20 LabSZ sshd[24680]: no information provided for user fztu ip 192.168.0.1",
        PID="24680",
        ip="192.168.0.1",
    )
    print(log)
    print(f"ip={log.getIPV4()}")


def zad_2():
    LogInvalid = LogInvalidPassword(
        user="webmaster",
        PID="24200",
        ip="172.234.31.186",
        port="38926",
        date=datetime(year=2024, day=10, month=12, hour=6, minute=55, second=48),
        rawLog="Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2",
    )
    logAccepted = LogPasswordAccepted(
        user="fztu",
        PID="24680",
        ip="119.137.62.142",
        port="49116",
        date=datetime(year=2024, day=11, month=12, hour=9, minute=32, second=20),
        rawLog="Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh2",
    )
    logError = LogError(
        PID="25455",
        ip="103.99.0.122",
        date=datetime(year=2024, day=12, month=12, hour=11, minute=3, second=44),
        rawLog="Dec 12 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 103.99.0.122: 14: No more user authentication methods available. [preauth]",
    )
    logOther = LogOther(
        PID="24680",
        date=datetime(year=2024, day=10, month=12, hour=9, minute=32, second=20),
        rawLog="Dec 10 09:32:20 LabSZ sshd[24680]: no information provided",
    )
    for log in [logAccepted, LogInvalid, logError, logOther]:
        print(log)


def zad_3_4_6():
    for entry in getEntryList():
        print(f"entry={entry}validate={entry.validate()}, hasIP={entry.hasIP}")
    for entry1, entry2 in itertools.product(getEntryList(), repeat=2):
        print(f"{entry1=}{entry2=}")
        if entry1 == entry2:
            print("Entries equal\n")
        elif entry1 < entry2:
            print("entry1 is less than entry2\n")
        else:
            print("entry1 is greater than entry2\n")


if __name__ == "__main__":
    # zad_1()
    # zad_2()
    # zad_3_4_6()
    # testJournal()
    duckTyping()
