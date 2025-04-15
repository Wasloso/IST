package test;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import main.model.*;

public class SerializationTest {

 	@org.junit.Test
    public void testShapeSerialization() {
        ShapeElement shapeElement = new ShapeElement(Color.RED, new Rectangle2D.Double(0, 0, 100, 100));
        String serialized = shapeElement.serialize();
        String expected = "ShapeElement;0.0;0.0;100.0;100.0;1.0;0.0;0.0;1.0;0.0;0.0;255;0;0;Rectangle";
        org.junit.Assert.assertEquals(expected, serialized);
    }

    @org.junit.Test
    public void testImageSerialization() {
        ImageElement imageElement = new ImageElement("src\\test\\testimg.png");
        String serialized = imageElement.serialize();
        String expected = "ImageElement;0.0;0.0;70.0;37.0;1.0;0.0;0.0;1.0;0.0;0.0;src\\test\\testimg.png";
        org.junit.Assert.assertEquals(expected, serialized);
    }

    @org.junit.Test
    public void testShapeDeserialization() {
        String serialized = "ShapeElement;0.0;0.0;100.0;100.0;1.0;0.0;0.0;1.0;0.0;0.0;255;0;0;Rectangle";
        PosterElement element = PosterElement.deserialize(serialized);
        org.junit.Assert.assertTrue(element instanceof ShapeElement);
        ShapeElement shapeElement = (ShapeElement) element;
 
        org.junit.Assert.assertEquals(new Rectangle2D.Double(0, 0, 100, 100), shapeElement.getShape());
    }

    @org.junit.Test
    public void testImageDeserialization() {
        String serialized = "ImageElement;0.0;0.0;70.0;37.0;1.0;0.0;0.0;1.0;0.0;0.0;src\\test\\testimg.png";
        PosterElement element = PosterElement.deserialize(serialized);
        org.junit.Assert.assertTrue(element instanceof ImageElement);
    }

    @org.junit.Test
    public void testSerializeAndDeserialize() {
        ShapeElement shapeElement = new ShapeElement(Color.RED, new Rectangle2D.Double(0, 0, 100, 100));
        String serialized = shapeElement.serialize();
        PosterElement deserialized = PosterElement.deserialize(serialized);
        org.junit.Assert.assertEquals(shapeElement.getClass(), deserialized.getClass());
        org.junit.Assert.assertEquals(shapeElement.getBounds(), deserialized.getBounds());
        org.junit.Assert.assertEquals(shapeElement.getColor(), ((ShapeElement) deserialized).getColor());
        org.junit.Assert.assertEquals(shapeElement.getShape(), ((ShapeElement) deserialized).getShape());
    }

}
