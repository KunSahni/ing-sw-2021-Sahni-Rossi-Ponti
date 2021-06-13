package it.polimi.ingsw.client.gui.guicontrollers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.scene.image.Image;

public abstract class JFXController {
    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    protected Image getImageFromPath(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }
}
