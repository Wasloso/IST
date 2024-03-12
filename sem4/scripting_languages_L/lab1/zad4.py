a: int = 0
h: int = 0

while a <= 0 or h <= 0:
    if a <= 0:
        a = int(input("Enter base: "))
        if a <= 0:
            print("Base must be a positive number")
            continue

    if h <= 0:
        h = int(input("Enter heigth: "))
        if h <= 0:
            print("Height must be a positive number")


print(f"Area: {1/2*a*h}")
