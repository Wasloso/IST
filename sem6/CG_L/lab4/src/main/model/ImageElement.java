package main.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageElement extends PosterElement {
    private String imagePath; 
    private BufferedImage image;

    public ImageElement(String imagePath) {
        this.imagePath = imagePath;
        try {
            this.image = ImageIO.read(new File(imagePath)); 
            System.out.println("Image loaded successfully");
        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
        }
        this.originalBounds = getBounds();

    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void draw(Graphics2D g2d) {
            BufferedImage image = getImage(); 
            g2d.drawImage(image, transform, null);
            drawSelected(g2d);
    }

    @Override
    public boolean contains(Point p) {
        try {
            AffineTransform inverse = transform.createInverse();
            Point2D local = inverse.transform(p, null);
            BufferedImage image = getImage(); 
            Rectangle imageBounds = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            return imageBounds.contains(local);
        } catch (NoninvertibleTransformException e) {
            return false;
        }
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder(super.serialize());
        sb.append(";").append(imagePath); 
        return sb.toString();
    }

    @Override
    public Rectangle getBounds() {
        BufferedImage image = this.image;
        return transform.createTransformedShape(new Rectangle(0, 0, image.getWidth(), image.getHeight())).getBounds();
        
    }

    @Override
    public PosterElement copy() {
        return new ImageElement(imagePath); 
    }
}
