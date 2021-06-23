package it.polimi.ingsw.client.gui.guicontrollers.mainmenu.footer;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.gui.FXMLResources;
import it.polimi.ingsw.client.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnectFooterController extends FooterController {
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectButton;

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
