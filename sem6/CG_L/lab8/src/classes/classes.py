from dataclasses import dataclass

import numpy as np


@dataclass
class Light:
    position: np.ndarray
    direction: np.ndarray
    ambient: tuple[float, float, float] = (0.0, 0.0, 0.0)
    diffuse: tuple[float, float, float] = (1.0, 1.0, 1.0)
    specular: tuple[float, float, float] = (1.0, 1.0, 1.0)


@dataclass
class FaceGroup:
    material: "Material"
    positions: np.ndarray
    normals: np.ndarray
    vbo_positions: int = 0
    vbo_normals: int = 0
    vbo_indices: int = 0
    use_vbo: bool = True


@dataclass
class Material:
    name: str
    ambient: tuple[float, float, float] = (0.0, 0.0, 0.0)
    diffuse: tuple[float, float, float] = (0.0, 0.0, 0.0)
    specular: tuple[float, float, float] = (0.0, 0.0, 0.0)
    shininess: float = 0.0
    illum: int = 0


@dataclass
class Scene:
    face_groups: list[FaceGroup]
    lights: list[Light] = None
    name: str = None
    center: np.ndarray = None
    radius: float = None
    min_corner: np.ndarray = None
    max_corner: np.ndarray = None
    diagonal: np.ndarray = None

    def __post_init__(self):
        all_positions = np.concatenate([fg.positions for fg in self.face_groups])
        self.min_corner = np.min(all_positions, axis=0)
        self.max_corner = np.max(all_positions, axis=0)
        self.center = (self.min_corner + self.max_corner) / 2
        self.diagonal = self.max_corner - self.min_corner
        self.radius = np.max(np.linalg.norm(all_positions - self.center, axis=1))
        self._prepare_lights()
        print(
            f"Scene '{self.name}' initialized with {len(self.face_groups)} face groups."
        )
        print(f"Scene center: {self.center}, radius: {self.radius}")
        print(f"Scene bounding box: {self.min_corner} to {self.max_corner}")
        print(f"Scene diagonal: {self.diagonal}")
        for light in self.lights:
            print(f"Light at {light.position}, direction: {light.direction}")

    def _prepare_lights(self):
        self.lights = []
        center = self.center
        diag = self.diagonal

        front_pos = center + np.array([0, 0, diag[2] * 1.5])
        front_dir = center - front_pos
        front_dir /= np.linalg.norm(front_dir)

        front_light = Light(
            position=front_pos,
            direction=front_dir,
            ambient=(0.1, 0.1, 0.1),
            diffuse=(0.8, 0.8, 0.8),
            specular=(1.0, 1.0, 1.0),
        )

        top_pos = center + np.array([0, diag[1] * 2.0, 0])
        top_dir = center - top_pos
        top_dir /= np.linalg.norm(top_dir)

        top_light = Light(
            position=top_pos,
            direction=top_dir,
            ambient=(0.05, 0.05, 0.05),
            diffuse=(0.6, 0.6, 0.6),
            specular=(0.5, 0.5, 0.5),
        )

        self.lights.extend([front_light, top_light])

    def __str__(self):
        return f"{self.__class__.__name__}(face_groups={len(self.face_groups)})"


@dataclass
class Camera:
    eye: np.ndarray
    target: np.ndarray
    resolution: tuple[int, int]
    top_left: np.ndarray
    dU: np.ndarray
    dV: np.ndarray

    def get_view_direction(self):
        return self.target - self.eye

    def get_up_vector(self):
        direction = self.get_view_direction()
        direction = direction / np.linalg.norm(direction)
        right = np.cross(direction, np.array([0.0, 1.0, 0.0], dtype=np.float32))
        right = right / np.linalg.norm(right)
        up = np.cross(right, direction)
        return up / np.linalg.norm(up)

    def get_right_vector(self):
        return np.cross(self.get_view_direction(), self.get_up_vector())

    def move(self, offset: np.ndarray):
        self.eye += offset
        self.target += offset
        self.top_left += offset

    def walk(self, step: float):
        direction = self.get_view_direction()
        direction /= np.linalg.norm(direction)
        self.move(direction * step)

    def pan(self, dx: float, dy: float):
        # right direction of the screen
        right = self.get_right_vector()
        right /= np.linalg.norm(right)
        # up direction of the screen
        up = self.get_up_vector()
        up /= np.linalg.norm(up)
        self.move(dx * right + dy * up)

    def yaw(self, angle_deg: float):
        """Left and right"""
        angle = np.radians(angle_deg)
        rot = self._rotation_matrix(self.get_up_vector(), angle)
        self.target = self.eye + rot @ (self.target - self.eye)
        self.top_left = self.eye + rot @ (self.top_left - self.eye)
        self.dU = rot @ self.dU

    def pitch(self, angle_deg: float):
        """Up and down"""
        angle = np.radians(angle_deg)
        rot = self._rotation_matrix(self.get_right_vector(), angle)
        self.target = self.eye + rot @ (self.target - self.eye)
        self.top_left = self.eye + rot @ (self.top_left - self.eye)
        self.dV = rot @ self.dV

    def orbit_horizontal(self, angle_deg: float):
        angle = np.radians(angle_deg)
        rot = self._rotation_matrix(self.get_up_vector(), angle)
        self.eye = self.target + rot @ (self.eye - self.target)
        self.top_left = self.target + rot @ (self.top_left - self.target)
        self.dU = rot @ self.dU

    def orbit_vertical(self, angle_deg: float):
        angle = np.radians(angle_deg)

        right = self.get_right_vector()
        rot = self._rotation_matrix(right, angle)
        self.eye = self.target + rot @ (self.eye - self.target)
        self.top_left = self.target + rot @ (self.top_left - self.target)
        self.dU = rot @ self.dU

    def _rotation_matrix(self, axis: np.ndarray, angle: float) -> np.ndarray:
        # taken from wikipedia https://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle
        axis = axis / np.linalg.norm(axis)
        x, y, z = axis
        c = np.cos(angle)
        s = np.sin(angle)
        return np.array(
            [
                [c + x * x * (1 - c), x * y * (1 - c) - z * s, x * z * (1 - c) + y * s],
                [y * x * (1 - c) + z * s, c + y * y * (1 - c), y * z * (1 - c) - x * s],
                [z * x * (1 - c) - y * s, z * y * (1 - c) + x * s, c + z * z * (1 - c)],
            ],
            dtype=np.float32,
        )

    @staticmethod
    def default_camera_for_scene(scene: Scene, resolution=(800, 600)) -> "Camera":
        eye = scene.center - scene.diagonal

        return Camera(
            eye=eye.astype(np.float32),
            target=scene.center.astype(np.float32),
            resolution=resolution,
            top_left=np.zeros(3, dtype=np.float32),
            dU=np.zeros(3, dtype=np.float32),
            dV=np.zeros(3, dtype=np.float32),
        )
