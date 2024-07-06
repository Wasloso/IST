import java.awt.*;
import java.util.*;
import java.util.List;

public class ConvexHull {
    private enum Turn {
        collinear,
        clockwise,
        counterClockwise
    }

    public static List<Point> solve(List<Point> points) throws IllegalArgumentException {
        if (points == null || points.size() < 3) {
            throw new IllegalArgumentException();
        }

        Point p0 = lowestPoint(points); //najnizszyy punky
        polarSort(points, p0);  //sortowanie polarne

        //System.out.println(points);

        Stack<Point> hull = new Stack<>();

        hull.push(points.get(0));
        hull.push(points.get(1));

        for (int i = 2; i < points.size() && !hull.isEmpty(); i++) {
            Point top = hull.pop();
            while (!hull.isEmpty() && (turn(hull.peek(), top, points.get(i)) != Turn.counterClockwise)) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points.get(i));
        }
        if (hull.size() < 3) throw new IllegalArgumentException(); // sa linearne

        hull.push(p0);


        return hull;
    }

//    private static boolean checkAllCollinear(List<Point> points) {
//        for (int i = 2; i < points.size(); i++) {
//            if (turn(points.get(0), points.get(1), points.get(i)) != Turn.collinear) return false;
//        }
//        return true;
//    }

    private static Turn turn(Point a, Point b, Point c) {
        float det = cross(a, b, c);
        if (det > 0) {
            return Turn.counterClockwise;
        } else if (det < 0) {
            return Turn.clockwise;
        } else {
            return Turn.collinear;
        }
    }

    private static Point lowestPoint(List<Point> points) {
        Point low = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point temp = points.get(i);
            if (temp.y < low.y || (temp.y == low.y && temp.x < low.x)) {
                low = temp;
            }
        }
        return low;
    }

    protected static void polarSort(List<Point> points, Point p0) {
        points.sort((a, b) -> {
            float crossProduct = cross(p0, a, b);
            if (crossProduct > 0) {
                return -1;
            } else if (crossProduct < 0) {
                return 1;
            } else {
                float distanceA = distance(p0, a);
                float distanceB = distance(p0, b);
                return Float.compare(distanceA, distanceB);
            }
        });
    }


    private static float cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    private static float distance(Point a, Point b) {
        return ((a.x - b.x)) * ((a.x - b.x)) + ((a.y - b.y)) * ((a.y - b.y));
    }
}
