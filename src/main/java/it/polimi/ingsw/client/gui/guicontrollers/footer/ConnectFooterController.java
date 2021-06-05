package it.polimi.ingsw.client.gui.guicontrollers.footer;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.gui.GUI;
import javafx.event.ActionEvent;
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
        ClientSocket clientSocket = new ClientSocket(ipField.getText(),
                Integer.parseInt(portField.getText()),
                gui.getDumbModel().getUpdatesHandler());
        clientSocket.connect();
        gui.setClientSocket(clientSocket);
    }
}
