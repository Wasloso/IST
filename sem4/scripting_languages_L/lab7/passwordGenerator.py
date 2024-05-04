import random
import string


class PasswordGenerator:
    def __init__(self, length, count, charset=None):
        self.length = length
        self.count = count
        self.charset = charset if charset else string.ascii_letters + string.digits

    def __iter__(self):
        return self

    def __next__(self):
        if self.count == 0:
            raise StopIteration
        self.count -= 1
        return self.generate_password()

    def generate_password(self):
        return "".join(random.choices(self.charset, k=self.length))


if __name__ == "__main__":
    gen = PasswordGenerator(10, 5)
    print(next(gen))
    print(next(gen))
    print(next(gen))
    print(next(gen))
    print(next(gen))
    try:
        print(next(gen))
    except StopIteration:
        print("No more values to generate")
    gen = PasswordGenerator(22, 8, string.ascii_letters + "!@#$%^&*()_+")
    for password in gen:
        print(password)
