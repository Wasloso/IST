package main;

import java.awt.Graphics2D;
import java.awt.Point;

public class Circle extends Shape {
    public int radius;

    public Circle(final Point start, final Point end, final int r, final int g, final int b) {
        super(start, end, r, g, b);
        this.center = start;
        updateRadius(end);

    }

    @Override
    public void updateEndPoint(Point newEnd) {
        this.end = newEnd;
        updateRadius(newEnd);
    }

    @Override
    public void updateCenter(Point newCenter) {
        int dx = newCenter.x - center.x;
        int dy = newCenter.y - center.y;
        this.start = newCenter;
        this.center = newCenter;
        this.end.x = end.x + dx;
        this.end.y = end.y + dy;

    }

    private void updateRadius(Point newEnd) {
        this.radius = (int) Math.sqrt(Math.pow(end.x - center.x, 2) + Math.pow(end.y - center.y, 2));
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(color);
        g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }
}
