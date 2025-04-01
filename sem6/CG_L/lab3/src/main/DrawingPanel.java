package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JPanel;

class DrawingPanel extends JPanel {
    private Controller controller;
    private Point startPoint = null;
    private Shape newShape = null;
    private Shape selectedShape = null;
    private SelectedPoint selectedPoint = SelectedPoint.NONE;

    public DrawingPanel(Controller controller) {
        this.controller = controller;
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                for (Shape shape : controller.getShapes()) {
                    selectedPoint = checkIsNearPoint(startPoint, shape, 10);
                    if (selectedPoint == SelectedPoint.NONE)
                        continue;
                    if (selectedPoint == SelectedPoint.CENTER && e.getButton() == MouseEvent.BUTTON3) {
                        drawXOR(shape, null, null);
                        controller.removeShape(shape);
                        return;
                    }
                    selectedShape = shape;
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    newShape = createNewShape(startPoint);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (newShape != null) {
                    if (newShape.getStart().equals(newShape.getEnd())) {
                        newShape = null;

                    } else {
                        controller.addShape(newShape);
                    }
                }
                selectedShape = null;
                startPoint = null;
                newShape = null;
                selectedPoint = SelectedPoint.NONE;

            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point newPoint = e.getPoint();
                if (selectedShape != null) {
                    switch (selectedPoint) {
                        case START:
                            drawXOR(selectedShape, newPoint, selectedShape::updateStartPoint);
                            break;
                        case END:
                            drawXOR(selectedShape, newPoint, selectedShape::updateEndPoint);
                            break;
                        case CENTER:
                            drawXOR(selectedShape, newPoint, selectedShape::updateCenter);
                            break;
                        default:
                            break;
                    }
                    return;
                }
                if (newShape != null) {
                    drawXOR(newShape, newPoint, newShape::updateEndPoint);
                }
            }
        });
    }

    private void drawXOR(Shape shape, Point newPoint, Consumer<Point> updateFunction) {

        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setXORMode(Color.WHITE);
        shape.draw(g2d);
        if (updateFunction != null) {
            updateFunction.accept(newPoint);
            shape.draw(g2d);
        }
        g2d.setPaintMode();
    }

    private Shape createNewShape(Point startPoint) {
        if (controller.getShapeType() == ShapeType.UNKNOWN) {
            return null;
        }
        return Shape.fromShapeType(controller.getShapeType(), startPoint, startPoint, controller.getR(),
                controller.getG(), controller.getB());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Shape shape : controller.getShapes()) {
            shape.draw(g2d);
        }
    }

    public void initShapes() {
        repaint();
    }

    private SelectedPoint checkIsNearPoint(Point point, Shape shape, int sensitivity) {
        if (point.distance(shape.getCenter()) <= sensitivity) {
            return SelectedPoint.CENTER;
        }
        if (point.distance(shape.getStart()) <= sensitivity && shape instanceof Line) {
            return SelectedPoint.START;
        }
        if (point.distance(shape.getEnd()) <= sensitivity && shape instanceof Line) {
            return SelectedPoint.END;
        }
        return SelectedPoint.NONE;

    }

    public BufferedImage getImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        return image;
    }

}

enum SelectedPoint {
    START, END, CENTER, NONE
}