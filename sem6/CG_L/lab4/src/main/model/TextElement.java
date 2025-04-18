package main.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TextElement extends PosterElement {
    private String text;
    public TextElement(String text) {
        this.text = text;
        this.originalBounds = new Rectangle2D.Double(0, 0, 30,20).getBounds2D(); 
        setPosition(0, 0);
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        g2d.transform(transform);
    
        g2d.setColor(Color.BLACK);
        int x = (int) originalBounds.getX();
        int y = (int) originalBounds.getY() + g2d.getFontMetrics().getAscent();
    
        g2d.drawString(text, x, y);
        g2d.setTransform(old);
        drawSelected(g2d);
    }

    @Override
    public boolean contains(Point p) {
        try {
            AffineTransform inverse = transform.createInverse();
            Point2D local = inverse.transform(p, null);
            return originalBounds.contains(local);
        } catch (NoninvertibleTransformException e) {
            return false;
        }
        
    }

    @Override
    public Rectangle getBounds() {
        Rectangle2D transformedBounds = transform.createTransformedShape(originalBounds).getBounds2D();
        return transformedBounds.getBounds();
    }

    @Override
    public PosterElement copy() {
        TextElement copy = new TextElement(text);
        copy.transform.setTransform(new AffineTransform(transform)); 
        return copy;
    }


    
}
