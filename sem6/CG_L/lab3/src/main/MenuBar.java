package main;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

import javax.swing.*;

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
        export.addActionListener(e -> controller.exportToPng());
        saveItem.addActionListener(e -> controller.saveToFile("vect"));
        loadItem.addActionListener(e -> controller.loadFromFile("vect"));
        newItem.addActionListener(e -> controller.newFile());
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(export);
        add(fileMenu);
    }

}