import java.awt.image.*;

public class Chessboard {
    public static void main(String[] args) {
        BufferedImage image;

        int x_res, y_res, size, r_even, g_even, b_even, r_odd, b_odd, g_odd;
        String filename;

        x_res = Integer.parseInt(args[0].trim());
        y_res = Integer.parseInt(args[1].trim());
        size = Integer.parseInt(args[2].trim());
        r_even = Integer.parseInt(args[3].trim());
        g_even = Integer.parseInt(args[4].trim());
        b_even = Integer.parseInt(args[5].trim());
        r_odd = Integer.parseInt(args[6].trim());
        g_odd = Integer.parseInt(args[7].trim());
        b_odd = Integer.parseInt(args[8].trim());
        filename = args[9].trim();

        if (!Utils.validateRes(x_res, y_res)) {
            System.out.println("Invalid resolution");
            return;
        }

        image = fromParams(x_res, y_res, size, r_even, g_even, b_even, r_odd, g_odd, b_odd);

        Utils.saveToFile(image, filename);
    }

    static BufferedImage fromParams(int x_res, int y_res, int size, int r_even, int g_even, int b_even, int r_odd,
            int g_odd, int b_odd) {
        BufferedImage image = new BufferedImage(x_res, y_res,
                BufferedImage.TYPE_INT_RGB);
        int color_even = Utils.int2RGB(r_even, g_even, b_even);
        int color_odd = Utils.int2RGB(r_odd, g_odd, b_odd);

        boolean even = false;

        for (int y = 0; y < y_res; y++) {
            if (y % size == 0)
                even = !even;
            for (int x = 0; x < x_res; x++) {
                if (x % size == 0)
                    even = !even;
                image.setRGB(x, y, even ? color_even : color_odd);

            }
        }
        return image;

    }
}
