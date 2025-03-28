package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

class DrawingPanel extends JPanel {
    private Controller controller;
    private Point startPoint = null;
    private Shape newShape = null; 
    private Shape selectedShape = null;
    
    public DrawingPanel(Controller controller) {
        this.controller = controller;
        setBackground(Color.WHITE);
        
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Shape shape : controller.getShapes()) {
                    if (isNearCenter(e.getPoint(), shape, 10)) {
                        selectedShape = shape;  
                        return;
                    }
                }
                startPoint = e.getPoint();  
                newShape = createNewShape(startPoint);  
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (newShape != null) {
                    controller.addShape(newShape);  
                    newShape = null;  
                }
                if (selectedShape != null) {
                    selectedShape = null;  
                }

            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point newPoint = e.getPoint();
                if (selectedShape != null) {
                    selectedShape.updateCenter(newPoint); 
                    repaint();  
                    return;
                } 
                if (newShape != null) {
                    newShape.updateEndPoint(newPoint);  
                    repaint();  
                }
            }
        });
    }
    
    private Shape createNewShape(Point startPoint) {
        if(controller.getShapeType() == ShapeType.UNKNOWN){
            return null;
        }
        return Shape.fromShapeType(controller.getShapeType(), startPoint, startPoint, controller.getR(),controller.getG(), controller.getB());
    }
    
 
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        for (Shape shape : controller.getShapes()) {

            shape.draw(g2d);
        }
        
        
        if (newShape != null) {
            newShape.draw(g2d);
        }
    }

    private boolean isNearCenter(Point point, Shape shape, int sensitivity) {
        return point.distance(shape.getCenter()) <= sensitivity;
    }

    public BufferedImage getImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        return image;
    }
}