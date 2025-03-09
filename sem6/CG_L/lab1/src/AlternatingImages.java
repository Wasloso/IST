import java.awt.image.BufferedImage;

public class AlternatingImages {
    public static void main(String[] args) {
        String imagePath1 = args[0].trim();
        String imagePath2 = args[1].trim();
        String outputFile = args[3].trim();
        PatternEnum pattern = PatternEnum.fromString(args[2].trim());
        BufferedImage image1 = Utils.imageFromPath(imagePath1);
        BufferedImage image2 = Utils.imageFromPath(imagePath2);
        if (!validateResolutions(image1, image2)) {
            throw new IllegalArgumentException("Image resolutions do not match.");
        }
        int x_res = image1.getWidth();
        int y_res = image1.getHeight();
        BufferedImage mask = Mask.buildMask(pattern, x_res, y_res);
        BufferedImage outputImage = new BufferedImage(x_res, y_res, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < y_res; y++) {
            for (int x = 0; x < x_res; x++) {
                int maskColor = mask.getRGB(x, y);
                int imageColor;
                ColorEnum color = ColorEnum.fromColor(maskColor);
                if (color == ColorEnum.white) {
                    imageColor = image1.getRGB(x, y);
                } else {
                    imageColor = image2.getRGB(x, y);
                }
                outputImage.setRGB(x, y, imageColor);

            }
        }
        Utils.saveToFile(outputImage, outputFile);

    }

    private static boolean validateResolutions(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth())
            return false;
        if (img1.getHeight() != img2.getHeight())
            return false;
        return true;
    }
}
