package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.network.clienttoserver.messages.AuthenticationMessage;
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
        String nickname = nicknameField.getText();
        parentController.setFooter(FXMLResources.LOADING_FOOTER);
        parentController.setLoadingFooterText(gameId == -1 ? "joining lobby" : "joining game " + gameId);
        parentController.getGui().setPersonalNickname(nickname);
        parentController.getGui().getClientSocket()
                .sendMessage(new AuthenticationMessage(nickname, gameId));
    }
}
