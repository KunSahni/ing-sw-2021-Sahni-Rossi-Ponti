package it.polimi.ingsw.client.gui.guicontrollers.mainmenu;


import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer.FooterController;
import it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer.LoadingFooterController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * JavaFX controller for the main menu.
 */
public class MainMenuController extends JFXController {
    @FXML
    private AnchorPane variablePane;
    private FooterController footerController;

    @FXML
    private void initialize() {
        setFooter(FXMLResources.CONNECT_FOOTER);
    }

    /**
     * Changes the footer by rendering the newly passed footer.
     * @param footer Identifier for the new footer that will get applied to the GUI.
     */
    public void setFooter(FXMLResources footer) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(footer.toPathString()));
        variablePane.getChildren().clear();
        try {
            variablePane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        footerController = loader.getController();
        footerController.setParentController(this);
    }

    /**
     * Edits the text displayed on the loading footer.
     * @param text Updated text.
     */
    public void setLoadingFooterText(String text) {
        ((LoadingFooterController) footerController).setLoadingFooterText(text);
    }
}
