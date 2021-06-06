package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameLeaderCardsChoiceAction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class InGamePersonalController extends JFXController {
    @FXML
    private Button confirmButton;
    @FXML
    private TextArea outcomeTextArea;
    @FXML
    private VBox personalBoardLayer;
    @FXML
    private VBox preGameLeaderCardSelectionLayer;
    @FXML
    private CheckBox leaderCardCheckBox1;
    @FXML
    private CheckBox leaderCardCheckBox2;
    @FXML
    private CheckBox leaderCardCheckBox3;
    @FXML
    private CheckBox leaderCardCheckBox4;
    private final ObservableSet<CheckBox> selectedPreGameLeaderCardCheckBoxes =
            FXCollections.observableSet();

    public void initLeaderCardsSelection(List<DumbLeaderCard> leaderCards) {
        initCheckBox(leaderCardCheckBox1, leaderCards.get(0));
        initCheckBox(leaderCardCheckBox2, leaderCards.get(1));
        initCheckBox(leaderCardCheckBox3, leaderCards.get(2));
        initCheckBox(leaderCardCheckBox4, leaderCards.get(3));
        confirmButton.setUserData(ConfirmButtonStrategy.PRE_GAME_LEADER_CARDS_CHOICE);
        preGameLeaderCardSelectionLayer.toFront();
    }

    private void initCheckBox(CheckBox checkBox, DumbLeaderCard card) {
        checkBox.setUserData(card);
        checkBox.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
            if (selectedNow) {
                selectedPreGameLeaderCardCheckBoxes.add(checkBox);
            } else {
                selectedPreGameLeaderCardCheckBoxes.remove(checkBox);
            }
        });
    }

    @FXML
    private void confirm() {
        switch ((ConfirmButtonStrategy) confirmButton.getUserData()) {
            case PRE_GAME_LEADER_CARDS_CHOICE -> pickLeaderCards();
        }
    }

    private void pickLeaderCards() {
        if (selectedPreGameLeaderCardCheckBoxes.size() != 2) {
            showOutcome("Select two cards.");
        } else {
            List<DumbLeaderCard> chosenCards = selectedPreGameLeaderCardCheckBoxes.stream()
                    .map(CheckBox::getUserData)
                    .map(object -> (DumbLeaderCard) object)
                    .collect(Collectors.toList());
            if (gui.getInputVerifier().canPickLeaderCards(chosenCards)) {
                gui.getClientSocket().sendAction(new PregameLeaderCardsChoiceAction(chosenCards));
                showOutcome("Confirmed.");
            } else {
                showOutcome("Invalid Selection");
            }
        }
    }

    private void showOutcome(String outcome) {
        Platform.runLater(() -> {
            outcomeTextArea.setText(outcome);
            try {
                Thread.sleep(1000);
                outcomeTextArea.setText("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
