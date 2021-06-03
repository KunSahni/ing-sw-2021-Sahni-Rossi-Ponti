package it.polimi.ingsw.client.gui.guicontrollers.footer;

import javafx.event.ActionEvent;
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
    private void playSingle(ActionEvent actionEvent) {
        sendCreateLobbyMessage(1);
    }

    @FXML
    private void playTwo(ActionEvent actionEvent) {
        sendCreateLobbyMessage(2);
    }

    @FXML
    private void playThree(ActionEvent actionEvent) {
        sendCreateLobbyMessage(3);
    }

    @FXML
    private void playFour(ActionEvent actionEvent) {
        sendCreateLobbyMessage(4);
    }


    private void sendCreateLobbyMessage(int size) {

    }
}
