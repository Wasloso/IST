package main;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Triangle {
    Vertex v1;
    Vertex v2;
    Vertex v3;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    static class Vertex {
        Point point;
        Color color;

        Vertex(int x, int y, Color color) {
            this.point = new Point(x, y);
            this.color = color;
        }

        Vertex(Point point, Color color) {
            this.point = point;
            this.color = color;
        }
    }
}