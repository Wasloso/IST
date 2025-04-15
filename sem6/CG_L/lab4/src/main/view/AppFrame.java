package main.view;

import javax.swing.*;

import main.controller.DragDropController;

import java.awt.*;

public class AppFrame extends JFrame {
    public AppFrame() {
        initialize();
    }

    private void initialize() {
        setTitle("My Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        PosterPanel posterPanel = new PosterPanel();
        DraggablePanel draggablePanel = new DraggablePanel(null);
        DragDropController controller = new DragDropController(posterPanel, draggablePanel);
        draggablePanel.setController(controller);
    
        ShapesPanel shapesPanel = new ShapesPanel(controller);
        ImagesPanel imagesPanel = new ImagesPanel(controller);
    
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.add(imagesPanel);
        leftPanel.add(shapesPanel);
    
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(posterPanel, BorderLayout.CENTER);
    
        
        draggablePanel.setOpaque(false);
        draggablePanel.setVisible(false); 
    
        
        setGlassPane(draggablePanel);
        getGlassPane().setVisible(false); 
    
        add(mainPanel);

        MenuPanel menuPanel = new MenuPanel(posterPanel);
        menuPanel.setAddImagesCallback(imagesPanel::addImage);
        setJMenuBar(menuPanel);
        setVisible(true);
    }
}