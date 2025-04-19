package main;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GouraudShader {

    public static void shade(Triangle triangle, BufferedImage image) {
        shade(triangle, new BufferedImageDrawer(image));
    }

    public static void shade(Triangle triangle, DrawingCallback drawer) {
        Triangle.Vertex[] sortedVertices = sortVertices(triangle);
        Triangle.Vertex top = sortedVertices[0];
        Triangle.Vertex middle = sortedVertices[1];
        Triangle.Vertex bottom = sortedVertices[2];

        int yu = (int) top.point.getY();
        int ym = (int) middle.point.getY();
        int yb = (int) bottom.point.getY();

        for (int y = yu; y <= yb; y++) {
            boolean isUpper = y <= ym;
            double betaL, betaR, xl, xr;
            Color cl, cr;
            if (isUpper) {
                betaL = getRatio(y, yu, ym);
                xl = interpolate(betaL, top.point.getX(), middle.point.getX());
                cl = interpolateColor(betaL, top.color, middle.color);
            } else {
                betaL = getRatio(y, ym, yb);
                xl = interpolate(betaL, middle.point.getX(), bottom.point.getX());
                cl = interpolateColor(betaL, middle.color, bottom.color);
            }
            betaR = getRatio(y, yu, yb);
            xr = interpolate(betaR, top.point.getX(), bottom.point.getX());
            cr = interpolateColor(betaR, top.color, bottom.color);
            scanLine(y, xl, xr, cl, cr, drawer);
        }

    }

    private static void scanLine(int y, double xl, double xr, Color cl, Color cr, DrawingCallback drawer) {
        if (xl > xr) {
            double tempX = xl;
            xl = xr;
            xr = tempX;
            Color tempC = cl;
            cl = cr;
            cr = tempC;
        }

        for (int x = (int) xl; x <= (int) xr; x++) {
            double alphaX = getRatio(x, xl, xr);
            Color color = interpolateColor(alphaX, cl, cr);
            drawer.draw(x, y, color);
        }
    }

    private static Triangle.Vertex[] sortVertices(Triangle triangle) {
        Triangle.Vertex[] verts = { triangle.v1, triangle.v2, triangle.v3 };
        java.util.Arrays.sort(verts, (a, b) -> Double.compare(a.point.getY(), b.point.getY()));
        return verts;
    }

    private static Color interpolateColor(double ratio, Color c1, Color c2) {
        int r = (int) interpolate(ratio, c1.getRed(), c2.getRed());
        int g = (int) interpolate(ratio, c1.getGreen(), c2.getGreen());
        int b = (int) interpolate(ratio, c1.getBlue(), c2.getBlue());
        return new Color(
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b)));
    }

    private static double interpolate(double ratio, double a, double b) {
        return a + ratio * (b - a);
    }

    private static double getRatio(double value, double start, double end) {
        if (start == end)
            return 0;
        return (value - start) / (end - start);
    }
}