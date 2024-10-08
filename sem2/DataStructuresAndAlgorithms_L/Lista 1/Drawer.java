package com.company;

public class Drawer {
    public static void drawTriangle(int size) {
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j <= i; j++) {
                    System.out.print("#");
                }
                System.out.println();
            }
        } else System.out.println("fail");
    }

    public static void drawSquare(int size) {
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                        System.out.print("#");
                    } else System.out.print(" ");
                }
                System.out.println();
            }
        } else System.out.println("fail");
    }

    public static void drawPyramid(int size) {
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 2 * size - 1; j++) {
                    if (j >= size - i - 1 && j <= size + i - 1) {
                        System.out.print("#");
                    } else if (j <= size + i - 1) System.out.print(" ");
                }
                System.out.println();
            }
        } else System.out.println("fail");
    }

    public static void drawChristmasTree(int size) {
        int addSpace;
        if (size > 0) {
            for (int k = 0; k < size; k++) {
                for (int i = 0; i < k + 1; i++) {
                    addSpace = size - k - 1;
                    while (addSpace != 0) {
                        System.out.print(" ");
                        addSpace--;
                    }
                    for (int j = 0; j < 2 * (k + 1) - 1; j++) {
                        if (j >= k - i && j <= k + i) {
                            System.out.print("#");
                        } else if (j <= k + i) System.out.print(" ");
                    }
                    System.out.println();
                }
            }
        } else System.out.println("fail");
    }

    public static void drawRectangle(int width, int height) {
        if (!(height <= 0)) {
            if (!(width <= 0)) {
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if ((i == 0 || i == height - 1) || (j == 0 || j == width - 1)) System.out.print("#");
                        else System.out.print(" ");
                    }
                    System.out.println();
                }
            } else System.out.println("fail");
        } else System.out.println("fail");

    }
}
