package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * JFX Controller for the loading footer.
 */
public class LoadingFooterController extends FooterController{
    @FXML
    private Label loadingLabel;

    /**
     * Changes the displayed loading text.
     * @param text Updated text.
     */
    public void setLoadingFooterText(String text) {
        loadingLabel.setText(text);
    }
}
