import sys
from src.loaders.cam_loader import read_cam_file
from src.loaders.obj_loader import load_obj_file
from src.classes.classes import Camera, Scene
from src.viewer.scene_viewer import SceneViewer  # Adjust import path as needed


def main():
    if len(sys.argv) < 2:
        print("Usage: python viewer.py <file.obj> (<cam.cam>)")
        sys.exit(1)

    obj_path = sys.argv[1]
    cam_path = sys.argv[2] if len(sys.argv) > 2 else None

    scene = load_obj_file(obj_path)
    camera = (
        read_cam_file(cam_path) if cam_path else Camera.default_camera_for_scene(scene)
    )

    viewer = SceneViewer(scene, camera)
    viewer.run()


if __name__ == "__main__":
    main()
