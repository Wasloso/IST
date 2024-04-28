import re
from ipaddress import ip_address
from abc import ABC, abstractmethod
from datetime import datetime
from time import strptime
from inspect import getmembers, isclass, isabstract

# wykorzystanie funkcji z poprzednich labów

from utils import log_to_tuple


class SSHLogEntry(ABC):

    def __init__(self, date, rawLog, PID, ip=None, user=None):
        self.date = date
        # w pythonie nie ma prawdziwie prywatnych atrybutow, ale konwencja jest taka, zeby oznaczac je podkreslnikiem _
        self._rawLog = rawLog
        self.PID = PID
        self.user = user
        self.ip = ip

    def getIPV4(self):
        pattern = r"[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}"
        ip = re.search(pattern, self._rawLog)
        try:
            return ip_address(ip[0]) if ip else None
            # if ip_address(ip).version == 4 else None # w tym przypadku nie musimy sprawdzac wersji poniewaz pattern gwarantuje ze bedzie to ipv4
        except:
            return None

    def getPort(self):
        pattern = r"port (\d*)"
        match = re.search(pattern, self._rawLog)
        return match.group(1) if match else None

    # bazowa implementacja metody abstrakcyjnej (w celu unikniecia powtarzaina kodu w klasach dziedziczacych)
    @abstractmethod
    def validate(self):
        (mon, day, time, _, PID, user, _, _, _) = log_to_tuple(self._rawLog)
        ip = str(self.getIPV4())
        time = datetime.strptime(time, "%H:%M:%S")
        # jako rok przyjmujemy aktualny rok bo w logach nie ma informacji o roku
        date = datetime(
            day=int(day),
            month=strptime(mon, "%b").tm_mon,
            year=datetime.now().year,
            hour=time.hour,
            minute=time.minute,
            second=time.second,
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
    def createLogEntry(log, **kwargs):
        if "Failed password" in log:
            return LogInvalidPassword(
                date=kwargs["date"],
                rawLog=kwargs["rawLog"],
                PID=kwargs["PID"],
                ip=kwargs["ip"],
                port=kwargs["port"],
                user=kwargs["user"],
            )
        if "Accepted password" in log:
            return LogPasswordAccepted(
                date=kwargs["date"],
                rawLog=kwargs["rawLog"],
                PID=kwargs["PID"],
                ip=kwargs["ip"],
                port=kwargs["port"],
                user=kwargs["user"],
            )
        if "error" in log:
            return LogError(
                date=kwargs["date"],
                rawLog=kwargs["rawLog"],
                PID=kwargs["PID"],
                ip=kwargs["ip"],
                user=kwargs["user"],
            )
        return LogOther(
            date=kwargs["date"],
            rawLog=kwargs["rawLog"],
            PID=kwargs["PID"],
            user=kwargs["user"],
            ip=kwargs["ip"],
        )

    # atrybuy tylko do odczytu
    @property
    def hasIP(self):
        return self.getIPV4() is not None

    # reprezentacja obiektu w postaci stringa - wypisanie wszystkich atrybutow
    def __repr__(self) -> str:
        return f"{type(self).__name__}\n" + "".join(
            f"{attr}: {self.__dict__[attr]}\n"
            for attr in self.__dict__
            if attr != "_rawLog"
        )

    # porownanie obiektow przez sprawdzenie czy sa tego samego typu i maja takie same wartosci atrybutow
    def __eq__(self, other):
        if type(other) != type(self):
            return False
        for attr in self.__dict__:
            if self.__dict__[attr] != other.__dict__[attr]:
                return False
        return True

    # less than jesli time obiektu jest mniejszy niz time obiektu z ktorym porownujemy
    def __lt__(self, other):
        return self.date < other.date

    # analogicznie do lt
    def __gt__(self, other):
        return self.date > other.date


class LogInvalidPassword(SSHLogEntry):
    # dodatkowe atrybutu wzgledem klasy bazowej to ip, port
    def __init__(self, date, rawLog, PID, ip=None, port=None, user=None):
        self.port = port
        super().__init__(date, rawLog, PID, ip, user)

    # dodatkowe sprawdzenie poprawnosci logu -> wywolanie metody validate z klasy bazowej
    def validate(self):
        port = self.getPort()
        if port != self.port:
            return False
        return self.checkFailed() and super().validate()

    # sprawdzenie czy w logu wystepuje informacja o niepoprawnym hasle
    def checkFailed(self):
        pattern = r"Failed password"
        return re.search(pattern, self._rawLog) is not None


class LogPasswordAccepted(SSHLogEntry):
    # dodatkowe atrybutu wzgledem klasy bazowej to ip i port
    def __init__(self, date, rawLog, PID, ip=None, port=None, user=None):
        self.port = port
        super().__init__(
            date,
            rawLog,
            PID,
            ip,
            user,
        )

    # dodatkowe sprawdzenie poprawnosci logu -> wywolanie metody validate z klasy bazowej
    def validate(self):
        ip = str(self.getIPV4())
        port = self.getPort()
        if self.ip != ip:
            return False
        if port != self.port:
            return False
        return self.checkAccepted() and super().validate()

    # sprawdzenie czy w logu wystepuje informacja o zaakceptowanym hasle
    def checkAccepted(self):
        pattern = r"Accepted password"
        return re.search(pattern, self._rawLog) is not None

    def createLogEntry(*args, **kwargs):
        return LogPasswordAccepted(*args, **kwargs)


class LogError(SSHLogEntry):
    def __init__(self, date, rawLog, PID, ip=None, user=None):
        super().__init__(date, rawLog, PID, ip, user)

    # dodatkowe sprawdzenie poprawnosci logu -> wywolanie metody validate z klasy bazowej
    def validate(self):
        ip = str(self.getIPV4())
        if ip != self.ip:
            return False
        return super().validate()

    def createLogEntry(*args, **kwargs):
        return LogError(*args, **kwargs)


class LogOther(SSHLogEntry):
    def __init__(self, date, rawLog, PID, ip=None, user=None):
        super().__init__(date, rawLog, PID, ip, user)

    def validate(self):
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
        user="fztu",
        PID="24680",
        ip="119.137.62.142",
        port="49116",
        date=datetime(year=2024, day=11, month=12, hour=9, minute=32, second=20),
        rawLog="Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.50 port 49116 ssh2",
    )

    print(l.validate())
    print(l.getIPV4())


if __name__ == "__main__":
    main()
