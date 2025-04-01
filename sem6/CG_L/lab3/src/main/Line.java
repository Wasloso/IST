package main;

import java.awt.Graphics2D;
import java.awt.Point;

public class Line extends Shape {
    public Line(Point start, Point end, int r, int g, int b) {
        super(start, end, r, g, b);
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(color);
        g.drawLine(start.x, start.y, end.x, end.y);
    }
}