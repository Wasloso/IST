import java.awt.image.*;

public class Rings {
    public static void main(String[] args) {

        final int w = 10;

        int x_res = Integer.parseInt(args[0].trim());
        int y_res = Integer.parseInt(args[1].trim());
        String outputFile = args[2].trim();

        BufferedImage image = fromParams(x_res, y_res, w);

        Utils.saveToFile(image, outputFile);
    }

    // Just for default blur parameter
    static BufferedImage fromParams(int x_res, int y_res, int width) {
        return fromParams(x_res, y_res, width, true);
    }

    static BufferedImage fromParams(int x_res, int y_res, int width, boolean blur) {
        BufferedImage image = new BufferedImage(x_res, y_res,
                BufferedImage.TYPE_INT_RGB);

        int x_c = x_res / 2;
        int y_c = y_res / 2;
        int black = Utils.int2RGB(0, 0, 0);
        int white = Utils.int2RGB(255, 255, 255);

        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                double d = Math.sqrt((y - y_c) * (y - y_c) + (x - x_c) * (x - x_c));
                if (blur) {
                    int intensity = (int) Utils.intensity(d, width);
                    int color = Utils.int2RGB(intensity, intensity, intensity);
                    image.setRGB(x, y, color);

                } else {
                    int r = (int) d / width;
                    if (r % 2 == 0) {
                        image.setRGB(x, y, black);
                    } else {
                        image.setRGB(x, y, white);
                    }

                }

            }
        }
        return image;
    }

}
