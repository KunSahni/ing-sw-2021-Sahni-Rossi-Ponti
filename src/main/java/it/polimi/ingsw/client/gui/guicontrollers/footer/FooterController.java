package it.polimi.ingsw.client.gui.guicontrollers.footer;

import it.polimi.ingsw.client.gui.guicontrollers.MainMenuController;

public abstract class FooterController {
    protected MainMenuController parentController;

    public void setParentController(MainMenuController parentController) {
        this.parentController = parentController;
    }
}
