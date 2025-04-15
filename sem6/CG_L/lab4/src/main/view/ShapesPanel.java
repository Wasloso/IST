package main.view;

import main.controller.DragDropController;
import main.model.ShapeElement;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;

public class ShapesPanel extends DraggablePosterElementsPanel<ShapeElement> {
    private Color currentColor = Color.BLACK; 
    private final List<ShapeElement> shapes = List.of(
        new ShapeElement(currentColor, new Rectangle2D.Double(0, 0, 50, 50)),
        new ShapeElement(currentColor, new Ellipse2D.Double(0, 0, 50, 50))
    );
    public ShapesPanel(DragDropController controller) {
        super(controller);
        contentPanel.setLayout(new GridLayout(0, 1, 10, 10));
        for (ShapeElement shape : shapes) {
            addElement(shape);
        }




        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Pick a Color", currentColor);
            if (chosen != null) {
                currentColor = chosen;
                refreshShapes(); 
            }
        });
        contentPanel.add(colorButton);
        contentPanel.revalidate();
    }
    private void refreshShapes() {
        for (ShapeElement shape : shapes) {
            shape.setColor(currentColor);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    

}
