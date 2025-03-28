package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Controller {
    int r;
    int g;
    int b;
    ShapeType shapeType;
    ArrayList<Shape> shapes;
    DrawingPanel drawingPanel = null;
    public void setDrawingPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
    public Controller(){
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.shapeType = ShapeType.UNKNOWN;
        this.shapes = new ArrayList<>();
    }

    void setColor(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    void setShapeType(final ShapeType shapeType) {
        this.shapeType = shapeType;
    }
    void setRGB(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    int getR() {
        return r;
    }
    int getG() {
        return g;
    }
    int getB() {
        return b;
    }
    ShapeType getShapeType() {
        return shapeType;
    }
    void setR(final int r) {
        this.r = r;
    }
    void setG(final int g) {
        this.g = g;
    }
    void setB(final int b) {
        this.b = b;
    }
    public void addShape(final Shape shape) {
        shapes.add(shape);
    }
    public ArrayList<Shape> getShapes() {
        return shapes;
    }
    public BufferedImage getImage() {
        return drawingPanel.getImage();
    }
    public void setShapes(final ArrayList<Shape> shapes) {
        clearShapes();
        this.shapes = shapes;
        if (this.drawingPanel != null) {
            this.drawingPanel.repaint(); 
        }
    }

    public void clearShapes() {
        this.shapes.clear();
        if ( this.drawingPanel != null) {
            this.drawingPanel.repaint(); 
        }
    }

    
}
