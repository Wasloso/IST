package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Line extends Shape {
    public Line(Point start, Point end, int r, int g, int b) {
        super(start, end, r, g, b);
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(color);
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Circle Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); 
        Point start = new Point(100, 100);
        Point end = new Point(120,20);
        Line line = new Line(start,end, 255, 0, 0);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                super.paintComponent(g);
                line.draw(g2d);
                g.fillOval(start.x - 5, start.y - 5, 10, 10);  
                g.fillOval(end.x - 5, end.y - 5, 10, 10);    
                
                
                
            }
        };
        frame.add(panel);
        frame.setVisible(true);

    }
}