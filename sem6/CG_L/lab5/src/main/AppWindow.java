package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class AppWindow extends JFrame {
    private int width;
    private int height;
    public TrianglesPanel appPanel;
    private Triangle triangleToDraw;

    public AppWindow(int width, int height) {
        this.width = width;
        this.height = height;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(width, height);
        setVisible(true);
        appPanel = new TrianglesPanel();
        appPanel.setSize(width, height);
        add(appPanel);
    }

    class TrianglesPanel extends JPanel {
        final List<Triangle> triangles = new ArrayList<>();

        public TrianglesPanel() {
            setLayout(null);
            setBackground(Color.WHITE);
            setVisible(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Triangle triangle : triangles) {
                shadeTriangle(triangle, g);
            }
        }

        public int addTriangle(Triangle triangle) {
            triangles.add(triangle);
            return shadeTriangle(triangle, null);
        }

        private int shadeTriangle(Triangle triangle, Graphics g) {
            Graphics G;
            if (g == null) {
                G = getGraphics();
            } else {
                G = g;
            }
            return GouraudShader.shade(triangle, (x, y, color) -> {
                G.setColor(color);
                G.fillRect(x, y, 1, 1);
            });
        }

        public void clearTriangles() {
            triangles.clear();
            repaint();
        }

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AppWindow window = new AppWindow(800, 600);

            for (Triangle triangle : Utils.generateTestTriangles(125)) {

                window.appPanel.addTriangle(triangle);
            }
        });

    }
}
