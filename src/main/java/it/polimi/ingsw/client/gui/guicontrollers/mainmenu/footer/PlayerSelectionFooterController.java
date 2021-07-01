package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.network.clienttoserver.messages.CreateLobbyMessage;
import javafx.fxml.FXML;

/**
 * JFX Controller for client side lobby size selection.
 */
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

    /**
     * Sends to the server the size of the lobby the player wishes to create.
     * @param size Integer representing the lobby's size.
     */
    private void sendCreateLobbyMessage(int size) {
        parentController.getGui().getClientSocket().sendMessage(new CreateLobbyMessage(size));
        parentController.setFooter(FXMLResources.LOADING_FOOTER);
        if (size == 1) {
            parentController.setLoadingFooterText("Setting up game");
        }
    }
}
