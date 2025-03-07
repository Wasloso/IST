import java.io.IOException;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;

public class Utils {
    static double intensity(double d, double w) {
        double intensity = 128 * (Math.sin(Math.PI * d / w) + 1);
        return Math.max(0, Math.min(255, intensity));
    }

    static int int2RGB(int red, int green, int blue) {

        red = red & 0x000000FF;
        green = green & 0x000000FF;
        blue = blue & 0x000000FF;

        return (red << 16) + (green << 8) + blue;
    }

    static boolean validateRes(int x_res, int y_res) {
        return x_res > 0 && y_res > 0;
    }

    static void saveToFile(RenderedImage image, String path) {
        try {
            ImageIO.write(image, "bmp", new File(path));
            System.out.println("Image created successfully");
        } catch (IOException e) {
            System.out.println("The image cannot be stored");
        }
    }

    static BufferedImage imageFromPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("The image cannot be read");
            return null;
        }
    }

}
