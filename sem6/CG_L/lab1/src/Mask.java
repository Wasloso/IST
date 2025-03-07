import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Mask {

    public static void main(String[] args) {
        String imagePath = args[0].trim();
        BufferedImage image = Utils.imageFromPath(imagePath);
        PatternEnum pattern = PatternEnum.fromString(args[1].trim());
        int r = Integer.parseInt(args[2].trim());
        int g = Integer.parseInt(args[3].trim());
        int b = Integer.parseInt(args[4].trim());
        int color = Utils.int2RGB(r, g, b);
        BufferedImage mask = null;
        int x_res = image.getWidth(), y_res = image.getHeight();

        switch (pattern) {
            case chessboard:
                mask = Chessboard.fromParams(x_res, y_res, 5, 0, 0, 0, 255, 255, 255);
                break;
            case grid:
                mask = Grid.fromParams(x_res, y_res, 10, 5, 50, 0, 0, 0, 255, 255, 255);
                break;
            case rings:
                mask = GrayRings.fromParams(x_res, y_res, 15, false);
                break;
            default:
                throw new IllegalStateException("Mask could not be created");

        }

        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                int maskPixel = mask.getRGB(x, y);

                int maskR = (maskPixel >> 16) & 0xFF;
                int maskG = (maskPixel >> 8) & 0xFF;
                int maskB = maskPixel & 0xFF;

                if (maskR == 0 && maskG == 0 && maskB == 0) {
                    image.setRGB(x, y, color);
                }
            }
        }

        Utils.saveToFile(image, "mask.bmp");
    }

    private enum PatternEnum {
        grid,
        rings,
        chessboard;

        static PatternEnum fromString(String s) {
            return switch (s) {
                case "grid" -> PatternEnum.grid;
                case "rings" -> PatternEnum.rings;
                case "chessboard" -> PatternEnum.chessboard;
                default -> throw new IllegalArgumentException(
                        "Invalid pattern: " + s + "\nUse either grid, rings or chessboard");
            };
        }
    }
}
