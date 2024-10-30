import subprocess

for i in range(3, 13):
    print(i)
    if i < 10:
        subprocess.run(
            [
                "python",
                "load_data.py",
                "-db",
                "rentals",
                "-f",
                f"data/historia_przejazdow_2021-0{i}.csv",
            ],
            shell=True,
        )
    else:
        subprocess.run(
            [
                "python",
                "load_data.py",
                "-db",
                "rentals",
                "-f",
                f"data/historia_przejazdow_2021-{i}.csv",
            ],
            shell=True,
        )
