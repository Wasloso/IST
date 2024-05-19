from __future__ import annotations
import re
from ipaddress import IPv4Address, ip_address
from abc import ABC, abstractmethod
from datetime import datetime
from time import strptime
from re import Match

# wykorzystanie funkcji z poprzednich labów

from utils import log_to_tuple


class SSHLogEntry(ABC):

    def __init__(
        self,
        rawLog: str,
    ) -> None:
        log = log_to_tuple(rawLog)
        if not log:
            raise ValueError("Invalid log")
        (mon, day, time, _, PID, user, ip, _, _) = log
        time_datatime: datetime = datetime.strptime(time, "%H:%M:%S")
        self.date: datetime = datetime(
            day=int(day),
            month=strptime(mon, "%b").tm_mon,
            year=datetime.now().year,
            hour=time_datatime.hour,
            minute=time_datatime.minute,
            second=time_datatime.second,
        )
        # w pythonie nie ma prawdziwie prywatnych atrybutow, ale konwencja jest taka, zeby oznaczac je podkreslnikiem _
        self._rawLog: str = rawLog
        self.PID: str = PID
        self.user: str | None = user
        self.ip: str | None = ip

    def getIPV4(self) -> IPv4Address | None:
        ip: Match[str] | None = re.search(
            r"[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}", self._rawLog
        )
        try:
            return (
                IPv4Address(ip_address(ip[0]))
                if ip and ip_address(ip[0]).version == 4
                else None
            )
            # if ip_address(ip).version == 4 else None # w tym przypadku nie musimy sprawdzac wersji poniewaz pattern gwarantuje ze bedzie to ipv4
        except:
            return None

    def getPort(self) -> str | None:
        match: Match[str] | None = re.search(r"port (\d*)", self._rawLog)
        return match.group(1) if match else None

    # bazowa implementacja metody abstrakcyjnej (w celu unikniecia powtarzaina kodu w klasach dziedziczacych)
    @abstractmethod
    def validate(self) -> bool:
        log = log_to_tuple(self._rawLog)
        if log is None:
            return False
        (mon, day, time, _, PID, user, _, _, _) = log
        ip: str = str(self.getIPV4())
        time_datatime: datetime = datetime.strptime(time, "%H:%M:%S")
        # jako rok przyjmujemy aktualny rok bo w logach nie ma informacji o roku
        date = datetime(
            day=int(day),
            month=strptime(mon, "%b").tm_mon,
            year=datetime.now().year,
            hour=time_datatime.hour,
            minute=time_datatime.minute,
            second=time_datatime.second,
        )
        if date != self.date:
            return False
        if PID != self.PID:
            return False
        if ip != self.ip:
            return False
        if user != self.user:
            return False
        return True

    # 'factory method', chociaz w pythonie nie wyglada to najlepiej w porownaniu do innych jezykow obiektowych
    @staticmethod
    def createLogEntry(rawLog) -> SSHLogEntry:
        if "Failed password" in rawLog:
            return LogInvalidPassword(rawLog=rawLog)
        if "Accepted password" in rawLog:
            return LogPasswordAccepted(rawLog=rawLog)
        if "error" in rawLog:
            return LogError(rawLog=rawLog)
        return LogOther(rawLog=rawLog)

    # atrybuy tylko do odczytu
    @property
    def hasIP(self) -> bool:
        return self.getIPV4() is not None

    # reprezentacja obiektu w postaci stringa - wypisanie wszystkich atrybutow
    def __repr__(self) -> str:
        return f"{type(self).__name__}\n" + "".join(
            f"{attr}: {self.__dict__[attr]}\n"
            for attr in self.__dict__
            if attr != "_rawLog"
        )

    # porownanie obiektow przez sprawdzenie czy sa tego samego typu i maja takie same wartosci atrybutow
    def __eq__(self, other) -> bool:
        if type(other) != type(self):
            return False
        for attr in self.__dict__:
            if self.__dict__[attr] != other.__dict__[attr]:
                return False
        return True

    # less than jesli time obiektu jest mniejszy niz time obiektu z ktorym porownujemy
    def __lt__(self, other) -> bool:
        return self.date < other.date

    # analogicznie do lt
    def __gt__(self, other) -> bool:
        return self.date > other.date


class LogInvalidPassword(SSHLogEntry):
    # dodatkowe atrybutu wzgledem klasy bazowej to ip, port
    def __init__(self, rawLog) -> None:
        log = log_to_tuple(rawLog)
        if not log:
            raise ValueError("Invalid log")
        (_, _, _, _, _, _, _, port, _) = log
        self.port = port
        super().__init__(rawLog)

    # dodatkowe sprawdzenie poprawnosci logu -> wywolanie metody validate z klasy bazowej
    def validate(self) -> bool:
        if self.getPort() != self.port:
            return False
        return self.checkFailed() and super().validate()

    # sprawdzenie czy w logu wystepuje informacja o niepoprawnym hasle
    def checkFailed(self) -> bool:
        return re.search(r"Failed password", self._rawLog) is not None


class LogPasswordAccepted(SSHLogEntry):
    # dodatkowe atrybutu wzgledem klasy bazowej to ip i port
    def __init__(self, rawLog) -> None:
        log = log_to_tuple(rawLog)
        if not log:
            raise ValueError("Invalid log")
        (_, _, _, _, _, _, _, port, _) = log
        self.port: str | None = port
        super().__init__(rawLog)

    # dodatkowe sprawdzenie poprawnosci logu -> wywolanie metody validate z klasy bazowej
    def validate(self) -> bool:
        ip: str | None = str(self.getIPV4())
        port: str | None = self.getPort()
        if self.ip != ip:
            return False
        if port != self.port:
            return False
        return self.checkAccepted() and super().validate()

    # sprawdzenie czy w logu wystepuje informacja o zaakceptowanym hasle
    def checkAccepted(self) -> bool:
        return re.search(r"Accepted password", self._rawLog) is not None


class LogError(SSHLogEntry):
    def __init__(
        self,
        rawLog: str,
    ) -> None:
        super().__init__(rawLog)

    # dodatkowe sprawdzenie poprawnosci logu -> wywolanie metody validate z klasy bazowej
    def validate(self) -> bool:
        if str(self.getIPV4()) != self.ip:
            return False
        return super().validate()


class LogOther(SSHLogEntry):
    def __init__(
        self,
        rawLog: str,
    ) -> None:
        super().__init__(rawLog)

    def validate(self) -> bool:
        return True


def main():
    # logAcceped = "Dec 10 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh2"
    # logPA0 = LogPasswordAccepted(
    #     time="09:32:20",
    #     PID="24680",
    #     ip="119.137.62.142",
    #     port="49116",
    #     user="fztu",
    #     rawLog=logAcceped,
    # )
    # logPA1 = LogPasswordAccepted(
    #     time="09:32:20",
    #     PID="24680",
    #     ip="119.137.62.142",
    #     port="49116",
    #     user="fztu",
    #     rawLog=logAcceped,
    # )
    # logFailed = "Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2"
    # logFP0 = LogInvalidPassword(
    #     time="06:55:48",
    #     PID="24200",
    #     ip="173.234.31.186",
    #     port="38926",
    #     user="webmaster",
    #     rawLog=logFailed,
    # )

    # # print(logPA0)
    # # print(logFP0.validate())
    # loga = SSHLogEntry.createLogEntry(
    #     "InvalidPassword",
    #     time="09:32:20",
    #     PID="24680",
    #     ip="119.137.62.142",
    #     port="49116",
    #     user="fztu",
    #     rawLog=logAcceped,
    # )
    l = LogPasswordAccepted(
        rawLog="Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.50 port 49116 ssh2",
    )
    d = LogInvalidPassword(
        "Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.50 port 49116 ssh2"
    )

    print(d.validate())
    print(d.getIPV4())


if __name__ == "__main__":
    main()
