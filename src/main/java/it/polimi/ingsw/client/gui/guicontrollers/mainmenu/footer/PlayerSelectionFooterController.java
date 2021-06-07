package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.network.clienttoserver.messages.CreateLobbyMessage;
import javafx.fxml.FXML;

public class PlayerSelectionFooterController extends FooterController {
    @FXML
    private void playSingle() {
        sendCreateLobbyMessage(1);
    }

    @FXML
    private void playTwo() {
        sendCreateLobbyMessage(2);
    }

    @FXML
    private void playThree() {
        sendCreateLobbyMessage(3);
    }

    @FXML
    private void playFour() {
        sendCreateLobbyMessage(4);
    }


    private void sendCreateLobbyMessage(int size) {
        parentController.getGui().getClientSocket().sendMessage(new CreateLobbyMessage(size));
        parentController.setFooter(FXMLResources.LOADING_FOOTER);
        if (size == 1) {
            parentController.setLoadingFooterText("Setting up game");
        }
    }
}
