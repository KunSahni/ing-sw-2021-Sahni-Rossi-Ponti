package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.guicontrollers.mainmenu.MainMenuController;

/**
 * Abstract class used to represent a footer controller.
 */
public abstract class FooterController {
    protected MainMenuController parentController;

    /**
     * Links the footer to a parent controller for command forwarding.
     */
    public void setParentController(MainMenuController parentController) {
        this.parentController = parentController;
    }
}
