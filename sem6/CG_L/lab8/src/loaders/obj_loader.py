import os
from src.loaders.mtl_loader import load_mtl_file
from src.classes.classes import (
    Material,
    Scene,
    FaceGroup,
)  


import numpy as np


def load_obj_file(path):
    obj_positions = []
    obj_normals = []

    faces_per_material: dict[str, list] = {}
    current_material_name = None
    materials = {}

    with open(path, "r") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            parts = line.split()
            key, values = parts[0], parts[1:]

            match key:
                case "v":
                    obj_positions.append(tuple(map(float, values)))
                case "vn":
                    obj_normals.append(tuple(map(float, values)))
                case "f":
                    face_vertex_indices = []
                    for v_str in values:
                        indices = v_str.split("/")
                        v_idx = int(indices[0]) - 1
                        n_idx = int(indices[2]) - 1 if len(indices) > 2 else None
                        face_vertex_indices.append((v_idx, n_idx))

                    mat = current_material_name or "default"
                    if mat not in faces_per_material:
                        faces_per_material[mat] = []
                    faces_per_material[mat].append(face_vertex_indices)
                case "mtllib":
                    mtl_path = os.path.join(os.path.dirname(path), values[0])
                    materials.update(load_mtl_file(mtl_path))
                case "usemtl":
                    current_material_name = values[0]

    obj_pos_array = np.array(obj_positions, dtype=np.float32)
    obj_norm_array = np.array(obj_normals, dtype=np.float32) if obj_normals else None

    face_groups = []
    default_normal = np.array([0.0, 0.0, 0.0], dtype=np.float32)

    # for vbo
    for mat_name, faces_list in faces_per_material.items():
        mat = materials.get(mat_name, Material(name=mat_name))
        group_verts = []
        group_norms = []

        for face in faces_list:
            v0_idx, n0_idx = face[0]
            v0_pos = obj_pos_array[v0_idx]
            v0_norm = (
                obj_norm_array[n0_idx]
                if obj_norm_array is not None and n0_idx is not None
                else None
            )

            for i in range(1, len(face) - 1):
                v1_idx, n1_idx = face[i]
                v1_pos = obj_pos_array[v1_idx]
                v1_norm = (
                    obj_norm_array[n1_idx]
                    if obj_norm_array is not None and n1_idx is not None
                    else None
                )

                v2_idx, n2_idx = face[i + 1]
                v2_pos = obj_pos_array[v2_idx]
                v2_norm = (
                    obj_norm_array[n2_idx]
                    if obj_norm_array is not None and n2_idx is not None
                    else None
                )

                if v0_norm is None or v1_norm is None or v2_norm is None:
                    edge1 = v1_pos - v0_pos
                    edge2 = v2_pos - v0_pos
                    face_normal = np.cross(edge1, edge2)
                    norm_len = np.linalg.norm(face_normal)
                    face_normal = (
                        face_normal / norm_len if norm_len > 0 else default_normal
                    )
                    v0_norm = face_normal if v0_norm is None else v0_norm
                    v1_norm = face_normal if v1_norm is None else v1_norm
                    v2_norm = face_normal if v2_norm is None else v2_norm
                    print(f"handled missing normals: {v0_norm}, {v1_norm}, {v2_norm}")

                group_verts.extend([v0_pos, v1_pos, v2_pos])
                group_norms.extend([v0_norm, v1_norm, v2_norm])

        final_positions = np.array(group_verts, dtype=np.float32)
        final_normals = np.array(group_norms, dtype=np.float32) if group_norms else None

        face_groups.append(
            FaceGroup(
                material=mat,
                positions=final_positions,
                normals=final_normals,
            )
        )

    return Scene(face_groups=face_groups, name=os.path.basename(path))


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 2:
        print("Usage: python obj_loader.py <path_to_obj_file>")
        sys.exit(1)

    path = sys.argv[1]
    object3d = load_obj_file(path)
    print(str(object3d))
    for group in object3d.face_groups:
        print(
            f"Group material: {group.material.name}, Vertices: {len(group.positions) // 3}, Normals: {len(group.normals) // 3 if group.normals is not None else 0}"
        )
