package it.polimi.ingsw.client.gui.guicontrollers.footer;

import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NicknameFooterController extends FooterController {
    @FXML
    private TextField nicknameField;
    @FXML
    private Button joinButton;

    @FXML
    private void sendAuthReply(ActionEvent actionEvent) {
        int gameId = parentController.getGui().getDumbModel().getGameID();
        parentController.getGui().getClientSocket()
                .sendMessage(new AuthenticationMessage(nicknameField.getText(), gameId));
        parentController.setFooter(FXMLResources.LOADING_FOOTER);
        parentController.setLoadingFooterText(gameId == -1 ? "joining lobby" : "joining game " + gameId);
    }
}
