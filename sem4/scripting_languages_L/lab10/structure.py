from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship, Session
from sqlalchemy import String, ForeignKey, create_engine, Integer
from typing import Optional, List


class Base(DeclarativeBase):
    pass


class Station(Base):
    __tablename__ = "stations"
    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    name: Mapped[Optional[str]] = mapped_column(
        String(255), unique=True, nullable=False
    )


class Bike(Base):
    __tablename__ = "bikes"
    id: Mapped[int] = mapped_column(Integer, primary_key=True)

    def __repr__(self):
        return f"<Bike id={self.id}>"


class Rental(Base):
    __tablename__ = "rentals"
    id: Mapped[int] = mapped_column(primary_key=True)

    bike_id: Mapped[int] = mapped_column(
        Integer, ForeignKey("bikes.id"), nullable=False
    )
    bike = relationship("Bike", foreign_keys=[bike_id])

    rental_station_id: Mapped[int] = mapped_column(Integer, ForeignKey("stations.id"))
    rental_station = relationship("Station", foreign_keys=[rental_station_id])

    return_station_id: Mapped[int] = mapped_column(Integer, ForeignKey("stations.id"))
    return_station = relationship("Station", foreign_keys=[return_station_id])

    duration: Mapped[int] = mapped_column(Integer, nullable=False)
