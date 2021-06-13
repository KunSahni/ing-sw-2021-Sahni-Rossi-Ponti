package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InGameOppController extends PlayerBoardController {
    @FXML
    private Button backToPersonalBoardButton;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        backToPersonalBoardButton.setOnAction(e -> gui.goToPersonalView());
    }

    public void initialize(GUI gui, String nickname) {
        super.setGui(gui);
        super.setNicknameLabel(nickname);
    }

    @Override
    protected void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {

    }
}
