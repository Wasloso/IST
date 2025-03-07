import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class GrayRings {
    public static void main(String[] args) {
        System.out.println("Ring pattern synthesis");

        BufferedImage image;

        int x_res, y_res;

        int x_c, y_c;

        int i, j;

        final int w = 10;

        x_res = Integer.parseInt(args[0].trim());
        y_res = Integer.parseInt(args[1].trim());

        image = new BufferedImage(x_res, y_res,
                BufferedImage.TYPE_INT_RGB);

        x_c = x_res / 2;
        y_c = y_res / 2;

        for (i = 0; i < y_res; i++) {
            for (j = 0; j < x_res; j++) {
                double d = Math.sqrt((i - y_c) * (i - y_c) + (j - x_c) * (j - x_c));
                int intensity = (int) Utils.intensity(d, w);
                int gray = Utils.int2RGB(intensity, intensity, intensity);
                image.setRGB(j, i, gray);

            }
        }

        try {
            ImageIO.write(image, "bmp", new File(args[2]));
            System.out.println("Ring image created successfully");
        } catch (IOException e) {
            System.out.println("The image cannot be stored");
        }
    }

}
