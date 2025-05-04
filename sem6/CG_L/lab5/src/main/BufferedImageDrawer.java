package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageDrawer implements DrawingCallback {
    private BufferedImage image;
    private int[] pixels;

    @Override
    public void draw(int x, int y, Color color) {
        if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            pixels[y * image.getWidth() + x] = color.getRGB();
        }
    }

    public BufferedImageDrawer(BufferedImage image) {
        this.image = image;
        this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        for (Triangle triangle : Utils.generateTestTriangles(150)) {
            GouraudShader.shade(triangle, image);
        }

        try {
            ImageIO.write(image, "png", new File("output.png"));
            System.out.println("Image saved as output.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
