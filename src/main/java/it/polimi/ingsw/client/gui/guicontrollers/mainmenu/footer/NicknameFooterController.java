package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NicknameFooterController extends FooterController {
    @FXML
    private TextField nicknameField;
    @FXML
    private Button joinButton;

    @FXML
    private void sendAuthReply() {
        int gameId = parentController.getGui().getDumbModel().getGameID();
        parentController.setFooter(FXMLResources.LOADING_FOOTER);
        parentController.setLoadingFooterText(gameId == -1 ? "joining lobby" : "joining game " + gameId);
        parentController.getGui().getDumbModel().setNickname(nicknameField.getText());
        parentController.getGui().getClientSocket()
                .sendMessage(new AuthenticationMessage(nicknameField.getText(), gameId));
    }
}
