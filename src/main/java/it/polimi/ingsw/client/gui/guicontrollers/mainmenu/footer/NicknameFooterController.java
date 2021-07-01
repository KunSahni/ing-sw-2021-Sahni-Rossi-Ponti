package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.network.clienttoserver.messages.AuthenticationMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * JFX Controller which handles nickname selection.
 */
public class NicknameFooterController extends FooterController {
    @FXML
    private TextField nicknameField;

    /**
     * Sends an authentication reply to the server communicating the gameId saved to disk and
     * the selected nickname.
     */
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
