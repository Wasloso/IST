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
)
import utils


class App(QMainWindow):
    def __init__(self):
        super().__init__()
        self.title = "Log app"
        self.logs = []
        self.setMinimumSize(1200, 600)
        self.initUI()

    def initUI(self):
        self.setWindowTitle(self.title)
        self.central_widget = QWidget()
        self.setCentralWidget(self.central_widget)
        self.initLayout()
        self.show()

    def initLayout(self):
        main_layout = QVBoxLayout(self.central_widget)
        main_layout.addLayout(self.file_explorer_layout())
        main_layout.addLayout(self.logs_view_layout())
        main_layout.addLayout(self.prev_next_buttons_layout())

    def logs_view_layout(self):
        layout = QHBoxLayout()
        log_list_layout = QVBoxLayout()
        log_list_layout.addLayout(self.date_pick_layout())
        log_list_layout.addWidget(self.logs_list())
        layout.addLayout(log_list_layout)
        layout.addLayout(self.logs_details_layout())
        return layout

    def logs_details_layout(self):
        layout = QVBoxLayout()
        layout.addLayout(self.remote_host_view())
        layout.addLayout(self.date_view())
        layout.addLayout(self.time_view())
        layout.addLayout(self.stats_method_view())
        layout.addLayout(self.resources_view())
        layout.addLayout(self.size_view())
        return layout

    def remote_host_view(self):
        layout = QHBoxLayout()
        label = QLabel()
        label.setText("Remote host")
        self.remote_host_field = QLineEdit()
        self.remote_host_field.setReadOnly(True)
        layout.addWidget(label)
        layout.addWidget(self.remote_host_field)
        return layout

    def date_view(self):
        layout = QHBoxLayout()
        label = QLabel()
        label.setText("Date")
        self.date_field = QLineEdit()
        self.date_field.setReadOnly(True)
        layout.addWidget(label)
        layout.addWidget(self.date_field)
        return layout

    def time_view(self):
        layout = QHBoxLayout()
        label = QLabel()
        label.setText("Time")
        self.time_field = QLineEdit()
        self.time_field.setReadOnly(True)
        timezone_label = QLabel()
        timezone_label.setText("Timezone")
        self.timezone_field = QLineEdit()
        self.timezone_field.setReadOnly(True)
        layout.addWidget(label)
        layout.addWidget(self.time_field)
        layout.addWidget(timezone_label)
        layout.addWidget(self.timezone_field)
        return layout

    def stats_method_view(self):
        layout = QHBoxLayout()
        label_status = QLabel()
        label_status.setText("Status code")
        self.status_field = QLineEdit()
        self.status_field.setReadOnly(True)
        layout.addWidget(label_status)
        layout.addWidget(self.status_field)
        label_method = QLabel()
        label_method.setText("Method")
        self.method_field = QLineEdit()
        self.method_field.setReadOnly(True)
        layout.addWidget(label_method)
        layout.addWidget(self.method_field)
        return layout

    def resources_view(self):
        layout = QHBoxLayout()
        label = QLabel()
        label.setText("Resources")
        self.resources_field = QLineEdit()
        self.resources_field.setReadOnly(True)
        layout.addWidget(label)
        layout.addWidget(self.resources_field)
        return layout

    def size_view(self):
        layout = QHBoxLayout()
        label = QLabel()
        label.setText("Size")
        self.size_field = QLineEdit()
        self.size_field.setReadOnly(True)
        layout.addWidget(label)
        layout.addWidget(self.size_field)
        return layout

    def date_pick_layout(self):
        date_pick_layout = QHBoxLayout()
        date_from_label = QLabel()
        date_from_label.setText("From")
        date_pick_layout.addWidget(date_from_label)
        self.date_pick_from = QDateEdit(calendarPopup=True)
        date_pick_layout.addWidget(self.date_pick_from)
        date_to_label = QLabel()
        date_to_label.setText("To")
        date_pick_layout.addWidget(date_to_label)
        self.date_pick_to = QDateEdit(calendarPopup=True)
        date_pick_layout.addWidget(self.date_pick_to)
        self.date_filter_button = QPushButton()
        self.date_filter_button.setText("Filter")
        self.date_filter_button.clicked.connect(self.set_date)
        self.date_filter_clear_button = QPushButton()
        self.date_filter_clear_button.setText("Clear")
        self.date_filter_clear_button.clicked.connect(self.clear_fiter)
        date_pick_layout.addWidget(self.date_filter_button)
        date_pick_layout.addWidget(self.date_filter_clear_button)
        return date_pick_layout

    def clear_fiter(self):
        self.update_logs_view(self.logs)

    def set_date(self):
        date_from = self.date_pick_from.date().toPyDate()
        date_to = self.date_pick_to.date().toPyDate()
        self.update_logs_view(utils.filterByDate(self.logs, date_from, date_to))

    def logs_list(self):
        self.log_list_widget = QListWidget()
        scroll_bar = QScrollBar()
        self.log_list_widget.setVerticalScrollBar(scroll_bar)
        self.log_list_widget.itemSelectionChanged.connect(self.update_log_details)
        return self.log_list_widget

    def prev_next_buttons_layout(self):
        layout = QHBoxLayout()
        self.previous_button = QPushButton()
        self.previous_button.setText("Previous")
        self.previous_button.setEnabled(False)
        self.previous_button.clicked.connect(self.previous_log)
        self.next_button = QPushButton()
        self.next_button.setText("Next")
        self.next_button.setEnabled(False)
        self.next_button.clicked.connect(self.next_log)
        layout.addWidget(self.previous_button)
        layout.addWidget(self.next_button)
        return layout

    def next_log(self):
        self.position += 1
        self.log_list_widget.setCurrentRow(self.position)
        self.log_list_widget.setCurrentItem(self.log_list_widget.currentItem())

    def previous_log(self):
        self.position -= 1
        self.log_list_widget.setCurrentRow(self.position)
        self.log_list_widget.setCurrentItem(self.log_list_widget.currentItem())

    def file_explorer_layout(self):
        layout = QHBoxLayout()
        self.file_explorer_name = QLineEdit()
        self.file_explorer_name.setReadOnly(True)
        layout.addWidget(self.file_explorer_name)
        file_explorer_button = QPushButton("Open...")
        file_explorer_button.clicked.connect(self.open_file)
        layout.addWidget(file_explorer_button)
        return layout

    def open_file(self):
        dlg = QFileDialog()
        dlg.setFileMode(QFileDialog.FileMode.ExistingFile)
        if dlg.exec():
            file = dlg.selectedFiles()[0]
            if file:
                self.file_explorer_name.setText(f"{file}")
                self.logs = utils.read_log(file)
                self.shown_logs = self.logs.copy()
                self.update_logs_view(self.shown_logs)

    def update_logs_view(self, list):
        self.log_list_widget.clearSelection()
        self.log_list_widget.clearFocus()
        self.log_list_widget.clear()
        self.position = 0
        self.first = 0
        self.last = len(list)
        self.next_button.setEnabled(True if self.last > 0 else False)
        self.previous_button.setEnabled(False)
        for log in list:
            item = QListWidgetItem()
            item.setData(1, log)
            item.setText(log["raw"])
            self.log_list_widget.addItem(item)

    def update_log_details(self):
        log = self.log_list_widget.currentItem().data(1)
        self.position = self.log_list_widget.currentRow()
        self.previous_button.setEnabled(True if self.position > 0 else False)
        self.next_button.setEnabled(True if self.position < self.last - 1 else False)
        self.remote_host_field.setText(log["host"])
        self.date_field.setText(log["date"].strftime("%Y-%m-%d"))
        self.time_field.setText(log["time"].strftime("%H:%M:%S"))
        self.status_field.setText(str(log["requestCode"]))
        self.method_field.setText(log["requestType"])
        self.resources_field.setText(log["path"])
        self.size_field.setText(str(log["bytes"]))
        self.timezone_field.setText(log["timezone"])


app = QApplication([])
window = App()
app.exec()
