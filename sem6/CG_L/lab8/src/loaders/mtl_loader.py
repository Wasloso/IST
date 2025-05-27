import os
import numpy as np
from src.classes.classes import Material
from PIL import Image


def load_mtl_file(path: str) -> dict[str, Material]:
    materials = {}
    current = None

    with open(path, "r") as file:
        for line in file:
            line = line.lower()
            if line.startswith("#") or not line.strip():
                continue
            line = line.split()
            token, values = line[0], line[1:]
            match token:
                case "newmtl":
                    name = values[0]
                    current = Material(name)
                    materials[name] = current
                case "map_kd":
                    texture_path = os.path.join(os.path.dirname(path), values[0])
                    if not os.path.exists(texture_path) or not os.path.isfile(
                        texture_path
                    ):
                        continue
                    current.diffuse = _load_texture_from_file(texture_path)
                case "ka":
                    current.ambient = tuple(map(float, values[0:3]))
                case "kd":
                    current.diffuse = tuple(map(float, values[0:3]))
                case "ks":
                    current.specular = tuple(map(float, values[0:3]))
                case "ns":
                    current.shininess = float(values[0])
                case "illum":
                    current.illum = int(values[0])

    return materials


def _load_texture_from_file(image_path: str) -> tuple[float, float, float]:
    with Image.open(image_path) as img:
        img = img.convert("RGB")
        np_img = np.array(img)
        avg = np_img.mean(axis=(0, 1))
        return tuple(avg.tolist())


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 2:
        print("Usage: python mtl_loader.py <path_to_mtl_file>")
        sys.exit(1)

    path = sys.argv[1]
    materials = load_mtl_file(path)
    for name, material in materials.items():
        print(str(material))
