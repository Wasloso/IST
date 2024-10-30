from sqlalchemy import create_engine, extract, label
from sqlalchemy.orm import sessionmaker
from sqlalchemy.sql import func
from structure import Rental, Station, Bike
from datetime import datetime


def avg_rental_duration(session, station_id: int):
    return round(
        session.query(func.avg(Rental.duration))
        .filter_by(rental_station_id=station_id)
        .scalar(),
        2,
    )


def avg_return_duration(session, station_id):
    return round(
        session.query(func.avg(Rental.duration))
        .filter_by(return_station_id=station_id)
        .scalar(),
        2,
    )


def unique_bikes(session, station_id):
    return (
        session.query(func.count(func.distinct(Rental.bike_id)))
        .filter(Rental.return_station_id == station_id)
        .scalar()
    )


def rentals_per_month(session, station_id):
    return (
        session.query(
            label(
                "month",
                extract("month", func.datetime(Rental.rental_date, "unixepoch")),
            ),
            func.count(Rental.id).label("count"),
        )
        .filter_by(rental_station_id=station_id)
        .group_by("month")
        .all()
    )
