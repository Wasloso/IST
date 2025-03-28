package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class VectorPaint extends JFrame {
    private MenuBar menuBar;
    private DrawingPanel drawingPanel;
    private Controller controller;
    public VectorPaint() {
        setTitle("Vector Pain");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        initializeComponents();
    }

    private void initializeComponents() {
        this.controller = new Controller();
        this.menuBar = new MenuBar(this.controller);
        add(menuBar, BorderLayout.SOUTH);
        this.drawingPanel = new DrawingPanel(this.controller);
        add(drawingPanel, BorderLayout.CENTER); 
        this.controller.setDrawingPanel(this.drawingPanel);

    }

    public static void main(String[] args) {
        VectorPaint vp = new VectorPaint();
        vp.setVisible(true);
    }
}