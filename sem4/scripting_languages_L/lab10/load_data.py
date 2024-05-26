from argparse import ArgumentParser
import os, sys
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import pandas as pd
from structure import Bike, Station, Rental
from datetime import datetime


def load_data():
    parser = ArgumentParser()
    parser.add_argument("-f", "--file", required=True)
    parser.add_argument("-db", "--database", type=str, required=True)
    args = parser.parse_args()
    database = args.database
    file = args.file

    if not os.path.exists(file):
        print(f"File {file} does not exist")
        sys.exit(1)

    if not os.path.exists(f"{database}.sqlite3"):
        print(f"Database {database} does not exist")
        sys.exit(1)

    engine = create_engine(f"sqlite:///{database}.sqlite3")
    session = sessionmaker(bind=engine)()

    data = pd.read_csv(file)

    for index, row in data.iterrows():
        rental_id = row["UID wynajmu"]
        bike_id = row["Numer roweru"]
        rental_date = row["Data wynajmu"]
        return_date = row["Data zwrotu"]
        rental_station_name = row["Stacja wynajmu"]
        return_station_name = row["Stacja zwrotu"]
        duration = row["Czas trwania"]

        bike = session.query(Bike).filter_by(id=bike_id).first()
        if not bike:
            bike = Bike(id=bike_id)
            session.add(bike)

        rental_station = (
            session.query(Station).filter_by(name=rental_station_name).first()
        )
        if not rental_station:
            rental_station = Station(name=rental_station_name)
            session.add(rental_station)
            session.flush()

        return_station = (
            session.query(Station).filter_by(name=return_station_name).first()
        )
        if not return_station:
            return_station = Station(name=return_station_name)
            session.add(return_station)
            session.flush()

        rental = Rental(
            id=rental_id,
            bike_id=bike_id,
            rental_station_id=rental_station.id,
            return_station_id=return_station.id,
            rental_date=datetime.strptime(rental_date, "%Y-%m-%d %H:%M:%S").timestamp(),
            return_date=datetime.strptime(return_date, "%Y-%m-%d %H:%M:%S").timestamp(),
            duration=duration,
        )
        session.add(rental)

    session.commit()
    session.close()


if __name__ == "__main__":
    load_data()
