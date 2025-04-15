package main.view;

import main.controller.DragDropController;
import main.model.PosterElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.*;
public class DraggablePanel extends JPanel {
    private PosterElement selectedElement;
    private Point offset;
    private DragDropController controller;

    public DraggablePanel(DragDropController controller) {
        this.controller = controller;
        setOpaque(false);
        setLayout(null);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedElement != null && controller != null) {
                    controller.onMouseDragged(e);
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedElement != null && controller != null) {
                    controller.onMouseReleased(e);;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectedElement != null) {
            Graphics2D g2d = (Graphics2D) g;
            selectedElement.draw(g2d);
        }
    }

    public void setSelectedElement(PosterElement element, Point posPoint) {
        this.selectedElement = element;
        repaint();
    }

    public void clearSelectedElement() {
        this.selectedElement = null;
        repaint();
    }

    public void setController(DragDropController controller) {
        this.controller = controller;
    }

    
    
}