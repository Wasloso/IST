import re


class SSHUser:
    def __init__(self, userName, lastLogin) -> None:
        self.userName = userName
        self.lastLogin = lastLogin

    # zakladamy ze nazwa musi sie skladac z lier lub cyfr i miec od 6 do 32 znakow
    def validate(self):
        match = re.match(r"(\w{6,32})", self.userName)
        return match is not None


if __name__ == "__main__":
    usr1 = SSHUser("aaaa", None)
    usr2 = SSHUser(" a ", None)
    usr3 = SSHUser(".a.", None)

    for usr in [usr1, usr2, usr3]:
        print(usr.validate())
