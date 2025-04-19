package main;

import java.awt.Color;

@FunctionalInterface
public interface DrawingCallback {
    void draw(int x, int y, Color color);
}