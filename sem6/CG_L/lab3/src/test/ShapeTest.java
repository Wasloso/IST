// package test;
// import static org.junit.jupiter.api.Assertions.*;
// import main.Shape;
// import main.Line;
// import java.awt.Color;
// import java.awt.Point;
// import java.security.InvalidParameterException;

// import org.junit.jupiter.api.Test;

// class ShapeTest {

// @Test
// void testLineSerialization() {
// Line line = new Line(new Point(10, 20), new Point(30, 40), 255, 128, 0);
// String serialized = line.serialize();

// assertEquals("LINE 10 20 30 40 255 128 0", serialized);
// }

// @Test
// void testLineDeserialization() {
// String data = "LINE 10 20 30 40 255 128 0";
// Shape shape = Shape.deserialize(data);

// assertTrue(shape instanceof Line);
// Line line = (Line) shape;

// assertEquals(new Point(10, 20), line.start);
// assertEquals(new Point(30, 40), line.end);
// assertEquals(new Color(255, 128, 0), line.color);
// }

// @Test
// void testInvalidDeserialization_UnknownShape() {
// String data = "TRIANGLE 10 20 30 40 255 255 255";
// Exception exception = assertThrows(IllegalArgumentException.class, () ->
// Shape.deserialize(data));

// assertEquals("Unsupported shape type", exception.getMessage());
// }

// @Test
// void testInvalidDeserialization_MalformedData() {
// String data = "LINE 10 20 30";
// Exception exception = assertThrows(InvalidParameterException.class, () ->
// Shape.deserialize(data));

// assertNotNull(exception);
// }

// @Test
// void testDeserializationWithOutOfBoundsColorValues() {
// String data = "LINE 50 60 70 80 300 -50 500";
// Shape shape = Shape.deserialize(data);

// assertTrue(shape instanceof Line);
// Line line = (Line) shape;

// assertEquals(new Color(255, 0, 255), line.color);
// }
// }
