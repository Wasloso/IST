public class Main {
    public static void main(String[] args) {
        Point p = new Point(1, 2);
        Debug.fields(p);
        Person person = new Person(22, "Name", 1.75, 70);
        Debug.fields(person);
        Circle circle = new Circle(22);
        Debug.fields(circle);
    }
}
