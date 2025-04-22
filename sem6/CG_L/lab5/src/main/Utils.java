package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Triangle generateRandomTriangle(Dimension bounds) {
        return new Triangle(generateRandomVertex(bounds), generateRandomVertex(bounds), generateRandomVertex(bounds));

    }

    private static Triangle.Vertex generateRandomVertex(Dimension bounds) {
        return new Triangle.Vertex(
                (int) (Math.random() * bounds.getWidth()),
                (int) (Math.random() * bounds.getHeight()),
                randomColor());
    }

    public static Triangle[] generateTestTriangles(int size) {
        int offset = 20;
        int spacing = size + offset;

        List<Triangle> triangles = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            triangles.add(createRightTriangle(i, 0, size, spacing, i));
        }

        for (int i = 0; i < 4; i++) {
            triangles.add(createEquilateralTriangle(i, 1, size, spacing, i));
        }

        for (int i = 0; i < 4; i++) {
            triangles.add(createAcuteTriangle(i, 2, size, spacing, i));
        }

        for (int i = 0; i < 4; i++) {
            triangles.add(createObtuseTriangle(i, 3, size, spacing, i));
        }

        return triangles.toArray(new Triangle[0]);
    }

    private static Triangle createRightTriangle(int col, int row, int size, int spacing, int rotationSteps) {
        int x = col * spacing;
        int y = row * spacing;

        Point[] points = {
                new Point(x, y + size),
                new Point(x + size, y + size),
                new Point(x, y)
        };

        rotatePoints(points, rotationSteps, x + size / 2, y + size / 2);

        return new Triangle(
                new Triangle.Vertex(points[0], randomColor()),
                new Triangle.Vertex(points[1], randomColor()),
                new Triangle.Vertex(points[2], randomColor()));
    }

    private static Triangle createEquilateralTriangle(int col, int row, int size, int spacing, int rotationSteps) {
        int x = col * spacing;
        int y = row * spacing;
        double height = size * Math.sqrt(3) / 2;

        Point[] points = {
                new Point(x, y + size),
                new Point(x + size, y + size),
                new Point(x + size / 2, (int) (y + size - height))
        };

        rotatePoints(points, rotationSteps, x + size / 2, y + size / 2);

        return new Triangle(
                new Triangle.Vertex(points[0], randomColor()),
                new Triangle.Vertex(points[1], randomColor()),
                new Triangle.Vertex(points[2], randomColor()));
    }

    private static Triangle createAcuteTriangle(int col, int row, int size, int spacing, int rotationSteps) {
        int x = col * spacing;
        int y = row * spacing;

        Point[] points = {
                new Point(x + size / 2, y),
                new Point(x, y + size),
                new Point(x + 3 * size / 4, y + 3 * size / 4)
        };

        rotatePoints(points, rotationSteps, x + size / 2, y + size / 2);

        return new Triangle(
                new Triangle.Vertex(points[0], randomColor()),
                new Triangle.Vertex(points[1], randomColor()),
                new Triangle.Vertex(points[2], randomColor()));
    }

    private static Triangle createObtuseTriangle(int col, int row, int size, int spacing, int rotationSteps) {
        int x = col * spacing;
        int y = row * spacing;

        Point[] points = {
                new Point(x + size / 4, y + size / 4),
                new Point(x + 3 * size / 4, y + size / 4),
                new Point(x + size / 2, y + size)
        };

        rotatePoints(points, rotationSteps, x + size / 2, y + size / 2);

        return new Triangle(
                new Triangle.Vertex(points[0], randomColor()),
                new Triangle.Vertex(points[1], randomColor()),
                new Triangle.Vertex(points[2], randomColor()));
    }

    private static void rotatePoints(Point[] points, int steps, int cx, int cy) {
        double angle = Math.toRadians(90 * steps);
        for (Point p : points) {
            int dx = p.x - cx;
            int dy = p.y - cy;

            int rotatedX = (int) (cx + dx * Math.cos(angle) - dy * Math.sin(angle));
            int rotatedY = (int) (cy + dx * Math.sin(angle) + dy * Math.cos(angle));

            p.setLocation(rotatedX, rotatedY);
        }
    }

    private static Color randomColor() {
        return new Color((int) (Math.random() * 0x1000000));
    }
}