package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class MenuBar extends JMenuBar {
    private Controller controller;

    public MenuBar(Controller controller) {
        this.controller = controller;
        initializeShapeButtons();
        initializeColorFields();
        initializeFileMenu();
        setLayout(new GridLayout());

    }

    private void initializeShapeButtons() {
        JToggleButton lineItem = new JToggleButton(ShapeType.LINE.toString());
        JToggleButton circleItem = new JToggleButton(ShapeType.CIRCLE.toString());
        JToggleButton rectangleItem = new JToggleButton(ShapeType.RECTANGLE.toString());
        lineItem.addActionListener(e -> {
            controller.setShapeType(ShapeType.LINE);
        });
        circleItem.addActionListener(e -> {
            controller.setShapeType(ShapeType.CIRCLE);
        });
        rectangleItem.addActionListener(e -> {
            controller.setShapeType(ShapeType.RECTANGLE);
        });

        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(lineItem);
        shapeGroup.add(circleItem);
        shapeGroup.add(rectangleItem);
        add(lineItem);
        add(circleItem);
        add(rectangleItem);
    }

    private void initializeColorFields() {
        JTextField rField = new JTextField(3);
        rField.setText(controller.getR() + "");
        JTextField gField = new JTextField(3);
        gField.setText(controller.getG() + "");
        JTextField bField = new JTextField(3);
        bField.setText(controller.getB() + "");
        rField.addFocusListener(rgbFocusListener(controller::setR));
        gField.addFocusListener(rgbFocusListener(controller::setG));
        bField.addFocusListener(rgbFocusListener(controller::setB));
        rField.addActionListener(rgbEnterListener(controller::setR));
        gField.addActionListener(rgbEnterListener(controller::setG));
        bField.addActionListener(rgbEnterListener(controller::setB));

        JLabel rLabel = new JLabel("R: ");
        JLabel gLabel = new JLabel("G: ");
        JLabel bLabel = new JLabel("B: ");
        JPanel rPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rPanel.add(rLabel);
        rPanel.add(rField);

        JPanel gPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gPanel.add(gLabel);
        gPanel.add(gField);

        JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bPanel.add(bLabel);
        bPanel.add(bField);

        add(rPanel);
        add(gPanel);
        add(bPanel);

    }

    private FocusListener rgbFocusListener(Consumer<Integer> setter) {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateAndUpdate((JTextField) e.getSource(), setter);
            }
        };
    }

    private ActionListener rgbEnterListener(Consumer<Integer> setter) {
        return e -> validateAndUpdate((JTextField) e.getSource(), setter);
    }

    private void validateAndUpdate(JTextField source, Consumer<Integer> setter) {
        int value;
        try {
            value = Integer.parseInt(source.getText().trim());
            value = Math.max(0, Math.min(255, value));
        } catch (NumberFormatException ex) {
            value = 0;
        }
        source.setText(String.valueOf(value));
        setter.accept(value);
    }

    private void initializeFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem export = new JMenuItem("Export");
        JMenuItem newItem = new JMenuItem("New");
        export.addActionListener(e -> exportToPng());
        saveItem.addActionListener(e -> saveToFile("vect"));
        loadItem.addActionListener(e -> loadFromFile("vect"));
        newItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(getParent(), "Are you sure you want to start a new drawing?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.clearShapes();
            }
        });
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(export);
        add(fileMenu);
    }

    private void saveToFile(String extension) {
        if (controller.getShapes().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No shapes to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(extension, extension));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int returnValue = fileChooser.showSaveDialog(getParent());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith("." + extension)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + extension);
            }
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(getParent(),
                        "File already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile, false))) {
                for (Shape shape : controller.getShapes()) {
                    String serializedShape = shape.serialize();
                    writer.write(serializedShape);
                    writer.newLine();
                }
                writer.flush();
                JOptionPane.showMessageDialog(getParent(), "Shapes saved successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(getParent(), "Error saving file: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void loadFromFile(String extension) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter(extension, extension));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        int returnValue = fileChooser.showOpenDialog(getParent());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith("." + extension)) {
                JOptionPane.showMessageDialog(getParent(), "Please select a ." + extension + " file",
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
                        Shape shape = Shape.deserialize(line);
                        System.out.println(shape.serialize());
                        shapes.add(shape);
                    } catch (InvalidParameterException e) {
                        JOptionPane.showMessageDialog(getParent(), "Invalid shape data: " + line + "\nSkipping entry",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                }
                scanner.close();
                controller.setShapes(shapes);
                JOptionPane.showMessageDialog(getParent(), "Shapes loaded successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(getParent(), "Error loading file: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToPng() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        fileChooser.setCurrentDirectory(new File("."));

        int returnValue = fileChooser.showSaveDialog(getParent());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
            }
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(getParent(),
                        "File already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            try {
                BufferedImage image = controller.getImage();
                ImageIO.write(image, "png", selectedFile);
                JOptionPane.showMessageDialog(getParent(), "Exported to PNG successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(getParent(), "Error exporting to PNG: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}