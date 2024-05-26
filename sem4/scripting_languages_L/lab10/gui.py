from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import (
    QApplication,
    QMainWindow,
    QVBoxLayout,
    QLineEdit,
    QGridLayout,
    QWidget,
    QVBoxLayout,
    QHBoxLayout,
    QPushButton,
    QLabel,
    QDateEdit,
    QListWidget,
    QScrollBar,
    QFileDialog,
    QListWidgetItem,
    QMessageBox,
)
from load_data import load_data
from create_database import create_database
import subprocess
import sys
from sqlalchemy import MetaData, create_engine
from sqlalchemy.orm import sessionmaker
from structure import Station
from sqlselect import (
    avg_rental_duration,
    avg_return_duration,
    unique_bikes,
    rentals_per_month,
)


class App(QMainWindow):
    def __init__(self):
        super().__init__()
        self.title = "SQLite3"
        self.setMinimumSize(800, 600)
        self.setMaximumSize(800, 600)
        self.database = None
        self.engine = None
        self.session = None
        self.initUI()

    def initUI(self):
        self.setWindowTitle(self.title)
        self.central_widget = QWidget()
        self.setCentralWidget(self.central_widget)

        main_layout = QVBoxLayout(self.central_widget)
        main_layout.addLayout(self.create_database_layout())
        main_layout.addLayout(self.open_database_layout())
        main_layout.addLayout(self.load_data_layout())
        main_layout.addWidget(self.station_list())
        main_layout.addLayout(self.sqlbuttons())

        self.show()

    def create_database_layout(self):
        layout = QHBoxLayout()
        self.create_name_input = QLineEdit()
        self.create_name_input.setPlaceholderText("Enter database name")
        layout.addWidget(self.create_name_input)
        layout.addWidget(self.create_database_button())
        return layout

    def create_database_button(self):
        def fun():
            if self.create_name_input.text().strip() == "":
                return QMessageBox.warning(
                    self,
                    "Error",
                    "Please enter a name for the database",
                    QMessageBox.StandardButton.Ok,
                )
            result = subprocess.run(
                [
                    "python",
                    "create_database.py",
                    "-db",
                    self.create_name_input.text(),
                ],
                shell=True,
                capture_output=True,
            )
            if result.returncode == 0:
                QMessageBox.information(
                    self,
                    "Success",
                    f"Database - {self.create_name_input.text()} created successfully",
                    QMessageBox.StandardButton.Ok,
                )
                self.database = self.create_name_input.text()
                self.engine = create_engine(f"sqlite:///{self.database}.sqlite3")
                self.session = sessionmaker(bind=self.engine)()
                self.load_data_button.setEnabled(True)
                self.update_station_list()
                if self.list.count() > 0:
                    self.avgRentalDuration.setEnabled(True)
                    self.avgReturnDuration.setEnabled(True)
                    self.uniqueBikes.setEnabled(True)
                    self.rentalsPerMonth.setEnabled(True)
                self.setWindowTitle(f"{self.title} - {self.database}")

        button = QPushButton("Create database")
        button.clicked.connect(fun)
        return button

    def open_database_layout(self):
        layout = QHBoxLayout()
        layout.addWidget(self.open_database_button())
        return layout

    def open_database_button(self):
        def fun():
            database = QFileDialog.getOpenFileName(
                self, "Open database", "", "SQLite3 files (*.sqlite3)"
            )
            if database[0]:
                self.database = database[0].removesuffix(".sqlite3")
                self.engine = create_engine(f"sqlite:///{self.database}.sqlite3")
                self.session = sessionmaker(bind=self.engine)()
                self.load_data_button.setEnabled(True)
                if self.session is not None:
                    QMessageBox.information(
                        self,
                        "Success",
                        "Database opened successfully",
                        QMessageBox.StandardButton.Ok,
                    )
                    self.update_station_list()
                    self.avgRentalDuration.setEnabled(True)
                    self.avgReturnDuration.setEnabled(True)
                    self.uniqueBikes.setEnabled(True)
                    self.rentalsPerMonth.setEnabled(True)
                    self.setWindowTitle(f"{self.title} - {self.database}")

        button = QPushButton("Open database")
        button.clicked.connect(fun)
        return button

    def load_data_layout(self):
        layout = QVBoxLayout()
        layout.addWidget(self.load_data_button())
        return layout

    def load_data_button(self):
        def fun():
            file = QFileDialog.getOpenFileName(
                self, "Load data", "", "CSV files (*.csv)"
            )
            if file[0]:
                print(self.database)
                result = subprocess.run(
                    ["python", "load_data.py", "-db", self.database, "-f", file[0]],
                    capture_output=True,
                    shell=True,
                )
                if result.returncode == 0:
                    QMessageBox.information(
                        self,
                        "Success",
                        "Data loaded successfully",
                        QMessageBox.StandardButton.Ok,
                    )
                    self.update_station_list()
                else:
                    QMessageBox.warning(
                        self,
                        "Error",
                        "An error occurred while loading data",
                        QMessageBox.StandardButton.Ok,
                    )

        self.load_data_button = QPushButton("Load data")
        self.load_data_button.setEnabled(False)
        self.load_data_button.clicked.connect(fun)
        return self.load_data_button

    def station_list(self):
        self.list = QListWidget()
        if not self.session:
            return self.list
        stations = self.session.query(Station).all()
        for station in stations:
            item = QListWidgetItem(station.name)
            self.list.addItem(item)
        self.list.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn)
        return self.list

    def update_station_list(self):
        self.list.clear()
        stations = self.session.query(Station).all()
        stations.sort(key=lambda x: x.name)
        for station in stations:
            item = QListWidgetItem(station.name)
            item.setData(1, station.id)
            item.setText(station.name)
            self.list.addItem(item)

    def sqlbuttons(self):
        layout = QVBoxLayout()
        self.avgRentalDuration = QPushButton("Average rental duration")
        self.avgReturnDuration = QPushButton("Average return duration")
        self.uniqueBikes = QPushButton("Unique bikes")
        self.rentalsPerMonth = QPushButton("Rentals per month")
        layout.addWidget(self.avgRentalDuration)
        layout.addWidget(self.avgReturnDuration)
        layout.addWidget(self.uniqueBikes)
        layout.addWidget(self.rentalsPerMonth)

        if not self.session:
            self.avgRentalDuration.setEnabled(False)
            self.avgReturnDuration.setEnabled(False)
            self.uniqueBikes.setEnabled(False)
            self.rentalsPerMonth.setEnabled(False)

        self.avgRentalDuration.clicked.connect(self.sql_button_clicked)
        self.avgReturnDuration.clicked.connect(self.sql_button_clicked)
        self.uniqueBikes.clicked.connect(self.sql_button_clicked)
        self.rentalsPerMonth.clicked.connect(self.sql_button_clicked)

        return layout

    def sql_button_clicked(self):
        station_id = self.list.currentItem().data(1)
        button = self.sender()
        if button.text() == "Average rental duration":
            result = avg_rental_duration(self.session, station_id)
        elif button.text() == "Average return duration":
            result = avg_return_duration(self.session, station_id)
        elif button.text() == "Unique bikes":
            result = unique_bikes(self.session, station_id)
        elif button.text() == "Rentals per month":
            result = rentals_per_month(self.session, station_id)
        QMessageBox.information(
            self,
            "Result",
            f"{button.text()} for {self.list.currentItem().text()}: {result}",
            QMessageBox.StandardButton.Ok,
        )


app = QApplication([])
window = App()
window.show()
app.exec()
