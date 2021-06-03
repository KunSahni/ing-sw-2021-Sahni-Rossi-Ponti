package it.polimi.ingsw.client.gui.guicontrollers.footer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class LoadingFooterController extends FooterController{
    @FXML
    private Label loadingLabel;
    @FXML
    private ProgressIndicator progressIndicator;

    public void setLoadingFooterText(String text) {
        loadingLabel.setText(text);
    }
}
