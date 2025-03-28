package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
        this.radius = (int) Math.sqrt(Math.pow(end.x - center.x, 2) + Math.pow(end.y - center.y, 2)) ;
    }

 

    @Override
    void draw(Graphics2D g) {
        g.setColor(color);
        g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Circle Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); 
        Point start = new Point(100, 100);
        Point end = new Point(120,20);
        Circle circle = new Circle(start,end, 255, 0, 0);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                super.paintComponent(g);
                circle.draw(g2d);
                g.fillOval(start.x - 5, start.y - 5, 10, 10);  
                g.fillOval(end.x - 5, end.y - 5, 10, 10);    
                
                
                
            }
        };
        frame.add(panel);
        frame.setVisible(true);

    }
    
}
