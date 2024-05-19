import pytest
from SSHLogEntry import (
    SSHLogEntry,
    LogInvalidPassword,
    LogPasswordAccepted,
    LogError,
    LogOther,
)
from datetime import datetime
from ipaddress import IPv4Address
from SSHLogJournal import SSHLogJournal


@pytest.mark.parametrize(
    "log, expected_time",
    [
        (
            "Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2",
            datetime.strptime("2024 Dec 10 06:55:48", "%Y %b %d %H:%M:%S"),
        ),
        (
            "Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh2",
            datetime.strptime("2024 Dec 11 09:32:20", "%Y %b %d %H:%M:%S"),
        ),
        (
            "Dec 12 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 103.99.0.122: 14: No more user authentication methods available. [preauth]",
            datetime.strptime("2024 Dec 12 11:03:44", "%Y %b %d %H:%M:%S"),
        ),
    ],
)
def test_time_extraction(log, expected_time):
    sshlog = SSHLogEntry.createLogEntry(log)
    assert sshlog.date == expected_time


@pytest.mark.parametrize(
    "log, expected_ip",
    [
        (
            "Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2",
            "173.234.31.186",
        ),
        (
            "Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu from 119.137.62.142 port 49116 ssh2",
            "119.137.62.142",
        ),
        (
            "Dec 12 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 119.137.62.140: 14: No more user authentication methods available. [preauth]",
            "119.137.62.140",
        ),
    ],
)
def test_ipv4_extraction(log, expected_ip):
    assert SSHLogEntry.createLogEntry(log).getIPV4() == IPv4Address(expected_ip)


@pytest.mark.parametrize(
    "log",
    [
        "Dec 12 11:03:44 LabSZ sshd[25455]: error: Received disconnect: 14: No more user authentication methods available. [preauth]",
        "Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu port 49116 ssh2",
        "Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmasterport 38926 ssh2",
    ],
)
def test_ipv4_missing(log):
    assert not SSHLogEntry.createLogEntry(log).hasIP


@pytest.mark.parametrize(
    "log, expected_type",
    [
        (
            "Dec 11 09:32:20 LabSZ sshd[24680]: Accepted password for fztu port 49116 ssh2",
            LogPasswordAccepted,
        ),
        (
            "Dec 10 09:32:20 LabSZ sshd[24680]: no information provided",
            LogOther,
        ),
        (
            "Dec 10 06:55:48 LabSZ sshd[24200]: Failed password for invalid user webmaster from 173.234.31.186 port 38926 ssh2",
            LogInvalidPassword,
        ),
        (
            "Dec 12 11:03:44 LabSZ sshd[25455]: error: Received disconnect from 103.99.0.122: 14: No more user authentication methods available. [preauth]",
            LogError,
        ),
    ],
)
def test_append_types(log, expected_type):
    journal = SSHLogJournal()
    journal.append(log)
    assert isinstance(journal[0], expected_type)
