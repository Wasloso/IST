import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class ProceduralPatterns {
    public static void main(String[] args) {
        int x_res = Integer.parseInt(args[0].trim());
        int y_res = Integer.parseInt(args[1].trim());

        BufferedImage rings = buildRings(x_res, y_res);
        Utils.saveToFile(rings, "rings.bmp");
    }

    static BufferedImage buildCirclesPattern(int x_res, int y_res) {
        BufferedImage image = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);
        int spacing = 15;
        int radius = 30;
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

    static BufferedImage buildRings(int x_res, int y_res) {
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

                int ringIndex = (int) (d / width);

                if (ringIndex % 2 == 0) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return image;
    }

}
