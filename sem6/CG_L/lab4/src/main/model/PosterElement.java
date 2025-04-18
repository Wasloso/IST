package main.model;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.*;
public abstract class PosterElement {
    protected AffineTransform  transform = new AffineTransform();
    private boolean isSelected = false;
    protected Rectangle2D originalBounds;
    private Point2D.Double[] vertices = new Point2D.Double[4]; 
    private Point2D.Double center = new Point2D.Double();  

    
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(";");
        sb.append(originalBounds.getX()).append(";").append(originalBounds.getY()).append(";");
        sb.append(originalBounds.getWidth()).append(";").append(originalBounds.getHeight());
        double[] matrix = new double[6];
        transform.getMatrix(matrix);
        for (double val : matrix) {
            sb.append(";").append(val);
        }
        return sb.toString();
    }

    public static PosterElement deserialize(String data) {
        String[] parts = data.split(";");
        String className = parts[0];
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double width = Double.parseDouble(parts[3]);
        double height = Double.parseDouble(parts[4]);

        double[] matrix = new double[6];
        for (int i = 0; i < 6; i++) {
            matrix[i] = Double.parseDouble(parts[5 + i]);
        }
        PosterElement element = switch (className) {
            case "ImageElement" -> {
                String imagePath = parts[11]; 
                yield new ImageElement(imagePath);
            }
            case "ShapeElement" -> {
                int r = Integer.parseInt(parts[11]);
                int g = Integer.parseInt(parts[12]);
                int b = Integer.parseInt(parts[13]);
                Color color = new Color(r, g, b);
                Shape shape;
                String shapeType = parts[14];
                if (shapeType.equalsIgnoreCase("Circle")) {
                    shape = new Ellipse2D.Double(x, y, width, height);
                } else if (shapeType.equals("Rectangle")) {
                    shape = new Rectangle2D.Double(x, y, width, height);
                } 
                 else {
                    throw new IllegalArgumentException("Unknown shape type: " + shapeType);
                }
                yield new ShapeElement(color, shape);
            }
            default -> throw new IllegalArgumentException("Unknown class name: " + className);
        };
        element.transform.setTransform(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
        element.originalBounds = new Rectangle2D.Double(x, y, width, height);
        element.setVertices();
        element.setCenter();
        
        return element;
    }

    public void move(double dx, double dy) {
        AffineTransform move = new AffineTransform();
        move.translate(dx, dy);
        move.concatenate(transform);
        transform = move;
        setVertices();
        setCenter();
    }

    public void rotate(double angle) {
        double cx = center.getX();
        double cy = center.getY();
        AffineTransform rotate = new AffineTransform();
        rotate.translate(cx, cy);
        rotate.rotate(angle);
        rotate.translate(-cx, -cy);
        rotate.concatenate(transform);
        transform = rotate;
        setVertices();
        setCenter();
    }
    public void scale(double sx, double sy) {
        double cx = center.getX();
        double cy = center.getY();
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.translate(cx, cy);
        scaleTransform.scale(sx, sy);
        scaleTransform.translate(-cx, -cy);
        scaleTransform.concatenate(transform);
        transform = scaleTransform;
        setVertices();
        setCenter();
    }
    public abstract void draw(Graphics2D g2d);

    public abstract boolean contains(Point p);

    public abstract Rectangle getBounds(); 
    public double getCenterX() {
        return transform.createTransformedShape(originalBounds).getBounds2D().getCenterX();
    }

    public double getCenterY() {
        return transform.createTransformedShape(originalBounds).getBounds2D().getCenterY();
    }
    public abstract  PosterElement copy();
    public void setPosition(int x, int y) {
        transform.setToTranslation(x, y);
        setVertices();
        setCenter();
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public void drawSelected(Graphics2D g2d) {
        if (!isSelected) return;
        g2d.setColor(Color.ORANGE);
        for (Point2D pt : this.vertices) {
            drawHandle(g2d, pt);
        }
        drawHandle(g2d, this.center);
    }

    private void setVertices() {
        Point2D.Double[] originalCorners = {
            new Point2D.Double(originalBounds.getMinX(), originalBounds.getMinY()), 
            new Point2D.Double(originalBounds.getMaxX(), originalBounds.getMinY()), 
            new Point2D.Double(originalBounds.getMinX(), originalBounds.getMaxY()),  
            new Point2D.Double(originalBounds.getMaxX(), originalBounds.getMaxY()) 
        };
    
        for (int i = 0; i < 4; i++) {
            vertices[i] = (Point2D.Double)transform.transform(originalCorners[i], null);
        }
    }
    


    private void setCenter() {
        Rectangle2D transformedBounds = transform.createTransformedShape(originalBounds).getBounds2D();
        center.setLocation(transformedBounds.getCenterX(), transformedBounds.getCenterY());
    }
    

    private void drawHandle(Graphics2D g2d, Point2D pt) {
        int r = 5; 
        int x = (int) pt.getX() - r;
        int y = (int) pt.getY() - r;
        g2d.fillOval(x, y, r * 2, r * 2);
    }

    public Point2D.Double[] getVertices() {
        return vertices;
    }

    public SelectedVertex getSelectedVertex(Point2D point) {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].distance(point) < 5) { 
                return SelectedVertex.values()[i];
            }
        }
        if (center.distance(point) < 5) {
            return SelectedVertex.CENTER;
        }
        return SelectedVertex.NONE;
    }

    public void scaleFromAnchor(Point2D anchor, double sx, double sy){
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.translate(anchor.getX(), anchor.getY());
        scaleTransform.scale(sx, sy);
        scaleTransform.translate(-anchor.getX(), -anchor.getY());
        scaleTransform.concatenate(transform);
        transform = scaleTransform;
        setVertices();
        setCenter();
    }

}
