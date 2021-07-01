package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.client.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * JFX Footer controller which handles connection to a server.
 */
public class ConnectFooterController extends FooterController {
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;

    /**
     * Connects to the server using the address and port passed by the user.
     */
    @FXML
    private void connectToServer() {
        GUI gui = parentController.getGui();
        parentController.setFooter(FXMLResources.LOADING_FOOTER);
        parentController.setLoadingFooterText("Loading...");
        ClientSocket clientSocket = new ClientSocket(ipField.getText(),
                Integer.parseInt(portField.getText()),
                gui.getDumbModel().getUpdatesHandler());
        clientSocket.connect();
        gui.setClientSocket(clientSocket);
    }
}
