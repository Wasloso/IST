import java.awt.image.*;

public class Chessboard {
    public static void main(String[] args) {
        BufferedImage image;
        boolean even = false;

        int x_res, y_res, size, r_even, g_even, b_even, r_odd, b_odd, g_odd, color_even, color_odd;

        x_res = Integer.parseInt(args[0].trim());
        y_res = Integer.parseInt(args[1].trim());
        size = Integer.parseInt(args[2].trim());
        r_even = Integer.parseInt(args[3].trim());
        g_even = Integer.parseInt(args[4].trim());
        b_even = Integer.parseInt(args[5].trim());
        r_odd = Integer.parseInt(args[6].trim());
        g_odd = Integer.parseInt(args[7].trim());
        b_odd = Integer.parseInt(args[8].trim());
        color_even = Utils.int2RGB(r_even, g_even, b_even);
        color_odd = Utils.int2RGB(r_odd, g_odd, b_odd);

        if (!Utils.validateRes(x_res, y_res)) {
            System.out.println("Invalid resolution");
            return;
        }

        image = new BufferedImage(x_res, y_res,
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < y_res; y++) {
            if (y % size == 0)
                even = !even;
            for (int x = 0; x < x_res; x++) {
                if (x % size == 0)
                    even = !even;
                image.setRGB(x, y, even ? color_even : color_odd);

            }
        }

        Utils.saveToFile(image, "chessboard.bmp");
    }
}
