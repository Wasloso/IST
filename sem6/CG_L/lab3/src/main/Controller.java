package main;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller {
    int r;
    int g;
    int b;
    ShapeType shapeType;
    ArrayList<Shape> shapes;
    DrawingPanel drawingPanel = null;

    public void setDrawingPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public Controller() {
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.shapeType = ShapeType.UNKNOWN;
        this.shapes = new ArrayList<>();
    }

    void setColor(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    void setShapeType(final ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    void setRGB(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    int getR() {
        return r;
    }

    int getG() {
        return g;
    }

    int getB() {
        return b;
    }

    ShapeType getShapeType() {
        return shapeType;
    }

    void setR(final int r) {
        this.r = r;
    }

    void setG(final int g) {
        this.g = g;
    }

    void setB(final int b) {
        this.b = b;
    }

    public void addShape(final Shape shape) {
        shapes.add(shape);
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public BufferedImage getImage() {
        return drawingPanel.getImage();
    }

    public void setShapes(final ArrayList<Shape> shapes) {
        clearShapes();
        this.shapes = shapes;
        if (this.drawingPanel != null) {
            this.drawingPanel.initShapes();
        }
    }

    public void clearShapes() {
        this.shapes.clear();
        if (this.drawingPanel != null) {
            this.drawingPanel.repaint();
        }
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public void exportToPng() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        fileChooser.setCurrentDirectory(new File("."));

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
            }
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(null,
                        "File already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            try {
                BufferedImage image = getImage();
                ImageIO.write(image, "png", selectedFile);
                JOptionPane.showMessageDialog(null, "Exported to PNG successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error exporting to PNG: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void saveToFile(String extension) {
        if (getShapes().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No shapes to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(extension, extension));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith("." + extension)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + extension);
            }
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(null,
                        "File already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile, false))) {
                for (Shape shape : getShapes()) {
                    String serializedShape = shape.serialize();
                    writer.write(serializedShape);
                    writer.newLine();
                }
                writer.flush();
                JOptionPane.showMessageDialog(null, "Shapes saved successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    void loadFromFile(String extension) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter(extension, extension));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith("." + extension)) {
                JOptionPane.showMessageDialog(null, "Please select a ." + extension + " file",
                        "Invalid File", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ArrayList<Shape> shapes = new ArrayList<>();
                Scanner scanner = new Scanner(selectedFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    try {
                        shapes.add(Shape.deserialize(line));
                    } catch (InvalidParameterException e) {
                        JOptionPane.showMessageDialog(null, "Invalid shape data: " + line + "\nSkipping entry",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }
                scanner.close();
                JOptionPane.showMessageDialog(null, "Shapes loaded successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                setShapes(shapes);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void newFile() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new drawing?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clearShapes();
        }

    }

}
