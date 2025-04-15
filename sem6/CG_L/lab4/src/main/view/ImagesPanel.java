package main.view;

import main.controller.DragDropController;
import main.model.ImageElement;

public class ImagesPanel extends DraggablePosterElementsPanel<ImageElement> {

    public ImagesPanel(DragDropController controller) {
        super(controller);
    }

    public void addImage(String path) {
        ImageElement imageElement = new ImageElement(path);
        addElement(imageElement); 
    }


}
