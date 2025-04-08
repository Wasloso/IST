package main;

import java.awt.Graphics2D;
import java.awt.Point;

public class Rectangle extends Shape {
    public int width;
    public int height;
    public int x;
    public int y;

    public Rectangle(Point start, Point end, int r, int g, int b) {
        super(start, end, r, g, b);
        this.x = Math.min(start.x, end.x);
        this.y = Math.min(start.y, end.y);
        this.width = Math.abs(end.x - start.x);
        this.height = Math.abs(end.y - start.y);

    }

    @Override
    public void updateEndPoint(Point newEnd) {
        super.updateEndPoint(newEnd);
        this.x = Math.min(start.x, end.x);
        this.y = Math.min(start.y, end.y);
        this.width = Math.abs(end.x - start.x);
        this.height = Math.abs(end.y - start.y);
    }

    @Override
    public void updateCenter(Point newCenter) {
        this.center = newCenter;
        this.x = center.x - (width / 2);
        this.y = center.y - (height / 2);
        this.start = new Point(x, y);
        this.end = new Point(x + width, y + height);
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }
}
