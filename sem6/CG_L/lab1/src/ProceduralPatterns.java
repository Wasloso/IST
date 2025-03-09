import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

public class ProceduralPatterns {
    public static void main(String[] args) {
        int x_res = Integer.parseInt(args[0].trim());
        int y_res = Integer.parseInt(args[1].trim());

        BufferedImage circles = buildCircles(x_res, y_res, 15, 30);
        Utils.saveToFile(circles, "circles.bmp");

        BufferedImage rings = buildConvergingRings(x_res, y_res);
        Utils.saveToFile(rings, "convergingRings.bmp");

        BufferedImage targets = buildTargets(x_res, y_res, 75);
        Utils.saveToFile(targets, "targets.bmp");

        BufferedImage tiltedChessboard = buildTiltedChessboard(x_res, y_res, 50);
        Utils.saveToFile(tiltedChessboard, "tiltedChessboard.bmp");

        BufferedImage rays = buildRays(x_res, y_res, 24);
        Utils.saveToFile(rays, "rays.bmp");

    }

    static BufferedImage buildCircles(int x_res, int y_res, int spacing, int radius) {
        BufferedImage image = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);

        int gridSize = 2 * radius + spacing;
        int x_c = (x_res / 2);
        int y_c = (y_res / 2);

        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                int relative_y = Math.abs(y - y_c) % gridSize;
                int relative_x = Math.abs(x - x_c) % gridSize;
                if (relative_x > radius + spacing)
                    relative_x = 2 * radius + spacing - relative_x;
                if (relative_y > radius + spacing)
                    relative_y = 2 * radius + spacing - relative_y;

                double distance = Math
                        .sqrt(Math.pow(relative_x, 2) + Math.pow(relative_y, 2));
                if (distance < radius) {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return image;
    }

    static BufferedImage buildTiltedChessboard(int x_res, int y_res, int diagonal) {
        if (diagonal <= 1) {
            throw new IllegalArgumentException("Invalid diagonal");
        }
        BufferedImage image = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);
        int x_c = (x_res / 2);
        int y_c = (y_res / 2);

        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                int relative_x = Math.abs(x_c - x) % diagonal;
                int relative_y = Math.abs(y_c - y) % diagonal;
                if (relative_x > diagonal / 2) {
                    relative_x = diagonal - relative_x;
                }
                if (relative_y > diagonal / 2) {
                    relative_y = diagonal - relative_y;
                }
                if (relative_x > relative_y) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return image;
    }

    static BufferedImage buildConvergingRings(int x_res, int y_res) {
        BufferedImage image = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);
        int x_c = x_res / 2;
        int y_c = y_res / 2;

        double minWidth = 1.0;
        double maxWidth = 10.0;
        double growthFactor = 0.005;

        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {

                double d = Math.sqrt((y - y_c) * (y - y_c) + (x - x_c) * (x - x_c));

                double width = minWidth + (maxWidth - minWidth) * (1 - Math.exp(-growthFactor * d));

                int r = (int) (d / width);

                if (r % 2 == 0) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return image;
    }

    static BufferedImage buildTargets(int x_res, int y_res, int size) {
        BufferedImage image = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);
        int x_c = x_res / 2;
        int y_c = y_res / 2;
        if (size < 10) {
            throw new InvalidParameterException("Size must be at least 10");
        }
        int width = size / 10;
        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                int relative_y = Math.abs(y - y_c) % size;
                int relative_x = Math.abs(x - x_c) % size;
                if (relative_x > size / 2)
                    relative_x = size - relative_x;
                if (relative_y > size / 2)
                    relative_y = size - relative_y;
                double distance = Math
                        .sqrt(Math.pow(relative_x, 2) + Math.pow(relative_y, 2));

                int r = (int) distance / width;

                if (r % 2 == 0) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }

            }
        }

        return image;
    }

    static BufferedImage buildRays(int x_res, int y_res, int count) {
        if (count < 2) {
            throw new InvalidParameterException("Count must be at least 2");
        }
        BufferedImage image = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);
        int x_c = x_res / 2;
        int y_c = y_res / 2;
        int changeAngle = (int) 360 / count;
        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                double angleDegrees = Math.atan2(y_c - y, x_c - x) * 180 / Math.PI;
                if (angleDegrees < 0) {
                    angleDegrees += 360;
                }
                if ((int) angleDegrees / changeAngle % 2 == 0) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());

                }
            }
        }

        return image;
    }

}
