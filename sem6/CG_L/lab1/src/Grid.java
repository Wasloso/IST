import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Grid {
    public static void main(String[] args) {
        System.out.println("Grid pattern synthesis");

        BufferedImage image;

        int x_res, y_res, width, x_spacing, y_spacing, r_grid, g_grid, b_grid, r_bg, g_bg, b_bg;

        x_res = Integer.parseInt(args[0].trim());
        y_res = Integer.parseInt(args[1].trim());
        width = Integer.parseInt(args[2].trim());
        x_spacing = Integer.parseInt(args[3].trim());
        y_spacing = Integer.parseInt(args[4].trim());
        r_grid = Integer.parseInt(args[5].trim());
        g_grid = Integer.parseInt(args[6].trim());
        b_grid = Integer.parseInt(args[7].trim());
        r_bg = Integer.parseInt(args[8].trim());
        g_bg = Integer.parseInt(args[9].trim());
        b_bg = Integer.parseInt(args[10].trim());
        if (!Utils.validateRes(x_res, y_res)) {
            System.out.println("Resolution out of bounds");
            return;
        }
        if (width < 1 || x_spacing < 1 || y_spacing < 1) {
            System.out.println("Width or spacing out of bounds");
            return;
        }
        image = fromParams(x_res, y_res, width, x_spacing, y_spacing, r_grid, g_grid, b_grid, r_bg, g_bg, b_bg);

        Utils.saveToFile(image, "grid.bmp");

    }

    static BufferedImage fromParams(int x_res, int y_res, int width, int x_spacing, int y_spacing, int r_grid,
            int g_grid, int b_grid, int r_bg, int g_bg, int b_bg) {
        BufferedImage image = new BufferedImage(x_res, y_res,
                BufferedImage.TYPE_INT_RGB);
        int gridColor = Utils.int2RGB(r_grid, g_grid, b_grid);
        int bgColor = Utils.int2RGB(r_bg, g_bg, b_bg);
        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                if (x % x_spacing < width || y % y_spacing < width) {
                    image.setRGB(x, y, gridColor);
                } else {
                    image.setRGB(x, y, bgColor);
                }
            }
        }

        return image;

    }
}
