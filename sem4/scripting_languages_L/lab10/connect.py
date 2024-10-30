import sqlite3

con = sqlite3.connect("rentals.sqlite3")
cursor = con.cursor()
cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
print(cursor.fetchall())

# view relationships
cursor.execute("PRAGMA foreign_key_list(rentals);")
print(cursor.fetchall())
