#include <GL/glut.h>
#include <cmath>
#include <iostream>

float angle = 0.0f;

const GLfloat white[] = {1.0f, 1.0f, 1.0f};
const GLfloat black[] = {0.0f, 0.0f, 0.0f};
const int boardSize = 8;
GLuint boardList;
struct Field
{
    static constexpr float size = 1.0f;
    static constexpr float y_scale = 0.1f;
    static constexpr GLfloat specular[] = {0.2f, 0.2f, 0.2f, 1.0f};
    static constexpr GLfloat shininess = 50.0f;

    static void draw(bool isWhite, GLfloat x, GLfloat y, GLfloat z)
    {
        glPushMatrix();
        glTranslatef(x, y, z);
        glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, isWhite ? white : black);
        glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
        glMaterialf(GL_FRONT, GL_SHININESS, shininess);
        glScaled(1.0f, y_scale, 1.0f);
        glutSolidCube(size);
        glPopMatrix();
    }
};

struct Piece
{
    static constexpr float size = 0.5f;
    static constexpr GLfloat specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
    static constexpr GLfloat shininess = 50.0f;

    static void draw(bool isWhite, GLfloat x, GLfloat y, GLfloat z)
    {
        glPushMatrix();
        glTranslatef(x, y, z);

        glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, isWhite ? white : black);
        glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
        glMaterialf(GL_FRONT, GL_SHININESS, shininess);
        for (int i = 0; i < 3; i++)
        {
            glPushMatrix();
            glTranslatef(0.0f, i * 0.3f, 0.0f);
            glScalef(1.0f, 0.5f, 1.0f);
            glutSolidSphere(size, 16, 16);
            glPopMatrix();
        }
        glPopMatrix();
    }
};

struct Board
{

    static void draw(int size)
    {
        std::cout << "Drawing board of size: " << size << std::endl;
        for (int x = 0; x < size; x++)
        {
            for (int z = 0; z < size; z++)
            {
                bool isWhite = (x + z) % 2 == 0;
                float posX = (x - size / 2) * Field::size;
                float posZ = (z - size / 2) * Field::size;
                float posY = Field::size * Field::y_scale;

                Field::draw(isWhite, posX, 0.0f, posZ);
                if (z < 2)
                {
                    Piece::draw(true, posX, posY, posZ);
                }
                if (z >= size - 2)
                {
                    Piece::draw(false, posX, posY, posZ);
                }
            }
        }
    }
};

void setupLights()
{
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);
    glEnable(GL_LIGHT1);

    GLfloat light0_position[] = {0.0f, 10.0f, 0.0f, 1.0f};
    GLfloat light0_diffuse[] = {0.9f, 0.9f, 0.9f, 1.0f};
    GLfloat light0_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
    GLfloat light0_direction[] = {0.0f, -1.0f, 0.0f};

    glLightfv(GL_LIGHT0, GL_POSITION, light0_position);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, light0_diffuse);
    glLightfv(GL_LIGHT0, GL_SPECULAR, light0_specular);
    glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, light0_direction);
    glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 45.0f);
    glLightf(GL_LIGHT0, GL_SPOT_EXPONENT, 20.0f);
}

void reshape(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(45.0, (GLfloat)w / (GLfloat)h, 0.1, 50.0);
    glMatrixMode(GL_MODELVIEW);
}

void setupCamera()
{
    float radius = 15.0f;
    float camX = sin(angle) * radius;
    float camZ = cos(angle) * radius;
    gluLookAt(camX, 8.0f, camZ, 0.0f, 0.0f, 0.0f, 0, 1, 0);
}

void display()
{
    glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();
    setupCamera();
    setupLights();

    glCallList(boardList);
    glutSwapBuffers();
    glFlush();
}

void timer(int value)
{
    angle += 0.01f;
    glutPostRedisplay();
    glutTimerFunc(16, timer, 0);
}

void init()
{
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_NORMALIZE);
    boardList = glGenLists(1);

    glNewList(boardList, GL_COMPILE);
    Board::draw(boardSize);
    glEndList();

    glutReshapeFunc(reshape);
    glMatrixMode(GL_PROJECTION);

    glLoadIdentity();
    gluPerspective(45.0, 1.0, 0.1, 50.0);

    glMatrixMode(GL_MODELVIEW);
}

int main(int argc, char **argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(800, 800);
    glutCreateWindow("Chessboard");

    init();

    glutDisplayFunc(display);
    glutTimerFunc(0, timer, 0);
    glutMainLoop();
    return 0;
}
