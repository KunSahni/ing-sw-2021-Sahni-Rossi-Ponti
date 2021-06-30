package it.polimi.ingsw.client.gui.guicontrollers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.server.model.ChangesHandler;
import javafx.scene.image.Image;

import java.io.File;
import java.util.logging.Logger;

public abstract class JFXController {
    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    protected Image getImageFromPath(String path) {
        File file = new File(ChangesHandler.getWorkingDirectory() + "/client" + path);
        return new Image(file.toURI().toString());
    }
}
