package main.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ShapeElement extends PosterElement {
    private Color color;
    private Shape shape;
    private Shape transformedShape;
    public ShapeElement(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
        this.originalBounds = shape.getBounds2D();
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.fill(transform.createTransformedShape(shape));
        drawSelected(g2d);
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder(super.serialize());
        sb.append(";").append(color.getRed()).append(";").append(color.getGreen()).append(";").append(color.getBlue()).append(";");
        if (shape instanceof Rectangle2D.Double) {
            sb.append("Rectangle");
        } else if (shape instanceof Ellipse2D.Double) {
            sb.append("Circle");
        } else {
            throw new UnsupportedOperationException("Unsupported shape type: " + shape.getClass().getSimpleName());
        }
            

        return sb.toString();
    }

    @Override
    public boolean contains(Point p) {
        try {
            AffineTransform inverse = transform.createInverse();
            Point2D local = inverse.transform(p, null);
            return shape.contains(local);
        } catch (NoninvertibleTransformException e) {
            return false;
        }
    }
    @Override
    public Rectangle getBounds() {
        return transform.createTransformedShape(shape).getBounds();
    }

    @Override
    public PosterElement copy() {
        return new ShapeElement(color, shape); 
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    
}
