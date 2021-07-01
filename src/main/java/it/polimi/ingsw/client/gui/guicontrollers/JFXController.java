package it.polimi.ingsw.client.gui.guicontrollers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.server.model.ChangesHandler;
import javafx.scene.image.Image;

import java.io.File;
import java.util.logging.Logger;

/**
 * Abstract class representing a JavaFX controller with the addition of a linked GUI Object.
 */
public abstract class JFXController {
    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    /**
     * Returns a JavaFX Image element from the given path.
     * @param path Disk location of the image to render.
     * @return Image to be used in rendering.
     */
    protected Image getImageFromPath(String path) {
        File file = new File(ChangesHandler.getWorkingDirectory() + "/client" + path);
        return new Image(file.toURI().toString());
    }
}
