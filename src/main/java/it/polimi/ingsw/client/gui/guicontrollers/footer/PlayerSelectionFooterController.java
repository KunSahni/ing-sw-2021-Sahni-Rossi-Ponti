package it.polimi.ingsw.client.gui.guicontrollers.footer;

import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlayerSelectionFooterController extends FooterController {
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button twoPlayersButton;
    @FXML
    private Button threePlayersButton;
    @FXML
    private Button fourPlayersButton;

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
        if (size == 1) {
            parentController.setLoadingFooterText("Setting up game");
        }
    }
}
