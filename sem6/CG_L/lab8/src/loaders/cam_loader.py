from src.classes.classes import Camera
import numpy as np


def read_cam_file(filepath) -> Camera:
    with open(filepath, "r") as f:
        lines = [list(map(float, line.strip().split())) for line in f if line.strip()]

    eye = np.array(lines[0], dtype=np.float32)
    target = np.array(lines[1], dtype=np.float32)
    resolution = tuple(map(int, lines[2]))
    top_left = np.array(lines[3], dtype=np.float32)
    dU = np.array(lines[4], dtype=np.float32)
    dV = np.array(lines[5], dtype=np.float32)

    return Camera(
        eye=eye,
        target=target,
        resolution=resolution,
        top_left=top_left,
        dU=dU,
        dV=dV,
    )
