from structure import Base
from sqlalchemy import create_engine
from argparse import ArgumentParser


def create_database():
    parser = ArgumentParser()
    parser.add_argument("-db", "--database", type=str, required=True)
    args = parser.parse_args()
    engine = create_engine(f"sqlite:///{args.database}.sqlite3")
    Base.metadata.create_all(engine)


if __name__ == "__main__":
    create_database()
