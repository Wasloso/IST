package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.security.InvalidParameterException;

import javax.swing.JComponent;

public abstract class Shape extends JComponent {
    public Point start;
    public Point end;
    public Point center;
    public Color color;
    public boolean selected = false;

    public Shape(final Point start, final Point end, final int r, final int g, final int b) {
        this.start = start;
        this.end = end;
        this.center = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
        this.color = new Color(r, g, b);
    }

    abstract void draw(Graphics2D g);

    void drawDebug(Graphics2D g) {
        g.setColor(color);
        this.draw(g);
        g.setColor(Color.RED);
        g.fillOval(start.x - 5, start.y - 5, 10, 10);
        g.fillOval(center.x - 5, center.y - 5, 10, 10);
        g.fillOval(end.x - 5, end.y - 5, 10, 10);
    }

    public String serialize() {
        return String.format("%s %d %d %d %d %d %d %d",
                getClass().getSimpleName().toUpperCase(),
                start.x, start.y, end.x, end.y,
                color.getRed(), color.getGreen(), color.getBlue());
    }

    public void updateEndPoint(final Point newEnd) {
        this.end = newEnd;
        this.center = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
    }

    public void updateStartPoint(final Point newStart) {
        this.start = newStart;
        this.center = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
    }

    public void updateCenter(final Point newCenter) {
        int dx = newCenter.x - center.x;
        int dy = newCenter.y - center.y;
        this.center = newCenter;
        this.start.x = start.x + dx;
        this.start.y = start.y + dy;
        this.end.x = end.x + dx;
        this.end.y = end.y + dy;

    }

    public static Shape deserialize(final String data) {
        final String[] parts = data.split(" ");
        if (parts.length != 8)
            throw new InvalidParameterException("Invalid arguments for shape deserialization");
        final ShapeType type = ShapeType.fromString(parts[0]);
        final Point start = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        final Point end = new Point(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
        final int r = Math.max(0, Math.min(255, Integer.parseInt(parts[5])));
        final int g = Math.max(0, Math.min(255, Integer.parseInt(parts[6])));
        final int b = Math.max(0, Math.min(255, Integer.parseInt(parts[7])));

        return fromShapeType(type, start, end, r, g, b);

    }

    public static Shape fromShapeType(ShapeType type, final Point start, final Point end, final int r, final int g,
            final int b) {
        return switch (type) {
            case CIRCLE -> new Circle(start, end, r, g, b);
            case RECTANGLE -> new Rectangle(start, end, r, g, b);
            case LINE -> new Line(start, end, r, g, b);
            default -> throw new IllegalArgumentException("Unsupported shape type");
        };
    }

    public Point getCenter() {
        return center;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
