package it.polimi.ingsw.client.gui.guicontrollers;

import it.polimi.ingsw.client.gui.GUI;

public abstract class JFXController {
    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }
}
