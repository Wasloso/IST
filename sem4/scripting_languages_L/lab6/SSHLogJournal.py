from typing import Any
from SSHLogEntry import (
    LogPasswordAccepted,
    LogInvalidPassword,
    LogError,
    LogOther,
    SSHLogEntry,
)
from utils import log_to_tuple
from datetime import datetime
from time import strptime


class SSHLogJournal:
    logTypes = {}

    def __init__(self):
        self.list: list[SSHLogEntry] = []

    # "Log", ["Month", "Day", "Time", "Host", "Process", "User", "IP", "Message"]
    def append(self, log: str):
        tuple = log_to_tuple(log)
        if tuple is None:  # niepoprawny log
            return
        (mon, day, time, _, PID, user, ip, port, _) = tuple
        time = datetime.strptime(time, "%H:%M:%S")
        date = datetime(
            day=int(day),
            month=strptime(mon, "%b").tm_mon,
            year=datetime.now().year,
            hour=time.hour,
            minute=time.minute,
            second=time.second,
        )
        self.list.append(
            SSHLogEntry.createLogEntry(
                log, date=date, rawLog=log, PID=PID, user=user, ip=ip, port=port
            )
        )

    # wszystkie argumenty sa opcjonalne, jesli zostana podane to logi beda kolejno filtrowane wg tych argumentow
    def filterLogs(
        self,
        dateFrom: datetime = None,
        dateTo: datetime = None,
        ip: str = None,
        user: str = None,
        count: int = None,
    ):
        list = self.list.copy()
        if dateFrom:
            list = [entry for entry in list if entry.date >= dateFrom]
        if dateTo:
            list = [entry for entry in list if entry.date <= dateTo]
        if ip:
            # tutaj trzeba sprawdzic czy log ma atrybut ip poniewaz nie wszystkie podklasy maja ten atrybut
            list = [
                entry for entry in list if (hasattr(entry, "ip") and entry.ip == ip)
            ]
        if user:
            list = [entry for entry in list if entry.user == user]
        return list[:count] if count else list

    def __len__(self):
        return len(self.list)

    def __iter__(self):
        return iter(self.list)

    # w tym pzypadku patrzymy czy ktorys log jest w liscie na podstawie _rawLog
    def __contains__(self, log: str):
        return any(log == entry._rawLog for entry in self.list)

    def __getattr__(self, attr):
        # liczby w ip sa oddzielane _ poniewaz nie mozna wywolac tej metody z kropkami np ip.192.168.0.1 tylko ip_192_168_0_1
        if attr.startswith("ip_"):
            ip = attr.split("_")
            ip = ".".join(ip[1:])
            return [
                entry for entry in self.list if hasattr(entry, "ip") and entry.ip == ip
            ]
        elif attr.startswith("index_"):
            index = int(attr.split("_")[1])
            return self.list[index]
        elif attr.startswith("date_"):
            date_str = attr.split("_")[1:]
            date = datetime(
                year=int(date_str[0]),
                month=int(date_str[1]),
                day=int(date_str[2]),
            )
            return [
                entry
                for entry in self.list
                if all(
                    [
                        entry.date.year == date.year,
                        entry.date.day == date.day,
                        entry.date.month == date.month,
                    ]
                )
            ]
        else:
            raise AttributeError(
                f"'{self.__class__.__name__}' object has no attribute '{attr}'"
            )

    # umozliwienie dostepu do elementow przez indeksy i slice
    def __getitem__(self, index):
        if isinstance(index, slice):
            return self.list[index]
        else:
            return self.list[index]


if __name__ == "__main__":
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
    # print(journal.filterLogs(user="fztu", count=1))
    print(len(journal))
