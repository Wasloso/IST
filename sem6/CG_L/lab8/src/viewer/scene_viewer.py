from OpenGL.GL import *
from OpenGL.GLUT import *
from OpenGL.GLU import *

from src.classes.classes import Camera, Scene


class SceneViewer:
    def __init__(self, scene: Scene, camera: Camera):
        self.scene = scene
        self.camera = camera
        self.window_width, self.window_height = camera.resolution

    def init_glut(self):
        glutInit()
        glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA | GLUT_DEPTH | GLUT_MULTISAMPLE)
        glutInitWindowSize(self.window_width, self.window_height)
        glutCreateWindow(b"Displaying " + self.scene.name.encode())
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_DEPTH_TEST)
        glShadeModel(GL_SMOOTH)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        gluPerspective(45.0, self.window_width / self.window_height, 0.1, 100.0)
        glMatrixMode(GL_MODELVIEW)
        self.setup_vbos()
        self.enable_lighting()

        glutDisplayFunc(self.display)
        glutIdleFunc(self.display)
        glutKeyboardFunc(self.keyboard_func)
        glutReshapeFunc(self.reshape)

    def enable_lighting(self):
        glEnable(GL_LIGHTING)
        glEnable(GL_DEPTH_TEST)
        for idx, light in enumerate([GL_LIGHT0, GL_LIGHT1]):
            glEnable(light)
            glLightfv(light, GL_AMBIENT, self.scene.lights[idx].ambient + (1.0,))
            glLightfv(light, GL_DIFFUSE, self.scene.lights[idx].diffuse + (1.0,))
            glLightfv(light, GL_SPECULAR, self.scene.lights[idx].specular + (1.0,))
            glLightfv(light, GL_POSITION, self.scene.lights[idx].position + (1.0,))
            glLightfv(
                light, GL_SPOT_DIRECTION, self.scene.lights[idx].direction + (0.0,)
            )

    def display(self):
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        glLoadIdentity()

        gluLookAt(*self.camera.eye, *self.camera.target, *self.camera.get_up_vector())

        self.draw_scene()

        glutSwapBuffers()

    def draw_scene(self):

        for group in self.scene.face_groups:
            mat = group.material
            glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat.ambient + (1.0,))
            glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat.diffuse + (1.0,))
            glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat.specular + (1.0,))
            glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, mat.shininess)

            glEnableClientState(GL_VERTEX_ARRAY)
            glEnableClientState(GL_NORMAL_ARRAY)

            glBindBuffer(GL_ARRAY_BUFFER, group.vbo_positions)
            glVertexPointer(3, GL_FLOAT, 0, None)

            glBindBuffer(GL_ARRAY_BUFFER, group.vbo_normals)
            glNormalPointer(GL_FLOAT, 0, None)

            total_vertices_in_group = len(group.positions)
            if total_vertices_in_group > 0:
                glDrawArrays(GL_TRIANGLES, 0, total_vertices_in_group)

            glDisableClientState(GL_VERTEX_ARRAY)
            glDisableClientState(GL_NORMAL_ARRAY)
            glBindBuffer(GL_ARRAY_BUFFER, 0)

    def setup_vbos(self):
        # binding buffers for each face group
        for group in self.scene.face_groups:
            group.vbo_positions = glGenBuffers(1)
            print(f"Generated VBO for positions: {group.vbo_positions}")
            glBindBuffer(GL_ARRAY_BUFFER, group.vbo_positions)
            glBufferData(
                GL_ARRAY_BUFFER,
                group.positions.nbytes,
                group.positions,
                GL_STATIC_DRAW,
            )

            group.vbo_normals = glGenBuffers(1)
            glBindBuffer(GL_ARRAY_BUFFER, group.vbo_normals)
            glBufferData(
                GL_ARRAY_BUFFER,
                group.normals.nbytes,
                group.normals,
                GL_STATIC_DRAW,
            )

        glBindBuffer(GL_ARRAY_BUFFER, 0)

    def reshape(self, width, height):
        self.window_width = width
        self.window_height = height
        glViewport(0, 0, width, height)
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        gluPerspective(45.0, width / height, 0.1, 100.0)
        glMatrixMode(GL_MODELVIEW)
        glutPostRedisplay()

    def keyboard_func(self, key, x, y):
        step = 0.1

        match key.decode("utf-8"):
            case "w":
                self.camera.pan(0, step)
            case "s":
                self.camera.pan(0, -step)
            case "a":
                self.camera.pan(-step, 0)
            case "d":
                self.camera.pan(step, 0)
            case "z":
                self.camera.walk(step)
            case "x":
                self.camera.walk(-step)
            case "[":
                self.camera.yaw(step * 5)
            case "]":
                self.camera.yaw(-step * 5)
            case ";":
                self.camera.pitch(-step * 5)
            case "'":
                self.camera.pitch(step * 5)
            case "t":
                self.camera.orbit_horizontal(step * 5)
            case "y":
                self.camera.orbit_horizontal(-step * 5)
            case "g":
                self.camera.orbit_vertical(step * 5)
            case "h":
                self.camera.orbit_vertical(-step * 5)

        glutPostRedisplay()

    def run(self):
        self.init_glut()
        glutMainLoop()
