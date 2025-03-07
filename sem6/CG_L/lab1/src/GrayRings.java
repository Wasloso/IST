import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class GrayRings {
    public static void main(String[] args) {
        System.out.println("Ring pattern synthesis");

        int x_res, y_res;

        final int w = 10;

        x_res = Integer.parseInt(args[0].trim());
        y_res = Integer.parseInt(args[1].trim());

        BufferedImage image = fromParams(x_res, y_res, w);

        Utils.saveToFile(image, "grayrings.bmp");
    }

    static BufferedImage fromParams(int x_res, int y_res, int width) {
        BufferedImage image = new BufferedImage(x_res, y_res,
                BufferedImage.TYPE_INT_RGB);

        int x_c = x_res / 2;
        int y_c = y_res / 2;

        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                double d = Math.sqrt((y - y_c) * (y - y_c) + (x - x_c) * (x - x_c));
                int intensity = (int) Utils.intensity(d, width);
                int gray = Utils.int2RGB(intensity, intensity, intensity);
                image.setRGB(x, y, gray);
            }
        }
        return image;
    }

}
