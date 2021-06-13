package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import javafx.scene.control.Button;

public enum ConfirmResetButtonsStrategy {
    NONE,
    PRE_GAME_LEADER_CARDS_CHOICE,
    PRE_GAME_RESOURCES_CHOICE,
    VISIT;

    public void applyTo(Button confirmButton, Button resetButton) {
        switch (this) {
            case NONE -> setStrategy(confirmButton, resetButton, null, false, false);
            case PRE_GAME_LEADER_CARDS_CHOICE -> setStrategy(confirmButton, resetButton, this, true, false);
            case PRE_GAME_RESOURCES_CHOICE -> setStrategy(confirmButton, resetButton, this, true, true);
            case VISIT -> setStrategy(confirmButton, resetButton, this, false, true);
        }
    }

    private void setStrategy(Button confirmButton, Button resetButton,
                            ConfirmResetButtonsStrategy strategy, boolean confirmVisibility,
                            boolean resetVisibility) {
        confirmButton.setUserData(strategy);
        resetButton.setUserData(strategy);
        confirmButton.setVisible(confirmVisibility);
        resetButton.setVisible(resetVisibility);
    }
}
