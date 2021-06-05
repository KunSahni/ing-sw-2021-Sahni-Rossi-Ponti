package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.guicontrollers.mainmenu.MainMenuController;

public abstract class FooterController {
    protected MainMenuController parentController;

    public void setParentController(MainMenuController parentController) {
        this.parentController = parentController;
    }
}
