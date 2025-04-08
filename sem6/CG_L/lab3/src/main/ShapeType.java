package main;

public enum ShapeType {
    CIRCLE("Circle"), RECTANGLE("Rectangle"), LINE("Line"), UNKNOWN("Unknown");

    private String name = null;

    ShapeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ShapeType fromString(String name) {
        for (ShapeType type : ShapeType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static ShapeType fromShape(Shape shape) {
        if (shape instanceof Circle) {
            return CIRCLE;
        } else if (shape instanceof Rectangle) {
            return RECTANGLE;
        } else if (shape instanceof Line) {
            return LINE;
        } else {
            return UNKNOWN;
        }
    }
}