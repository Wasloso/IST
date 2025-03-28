package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Circle Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        Point start = new Point(100, 100);
        Point end = new Point(120,20);
        Rectangle circle = new Rectangle(start,end, 255, 0, 0);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                super.paintComponent(g);
                circle.draw(g2d);
                g.fillOval(start.x - 5, start.y - 5, 10, 10);  // Start point
                g.fillOval(end.x - 5, end.y - 5, 10, 10);    // End point
                
                
                
            }
        };
        frame.add(panel);
        frame.setVisible(true);

    }
}
