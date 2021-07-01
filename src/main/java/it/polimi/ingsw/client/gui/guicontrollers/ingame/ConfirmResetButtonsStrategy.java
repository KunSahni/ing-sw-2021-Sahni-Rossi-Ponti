package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import javafx.scene.control.Button;

/**
 * Enum used as strategy pattern for confirmation and reset buttons in GUI controllers.
 */
public enum ConfirmResetButtonsStrategy {
    NONE,
    PRE_GAME_LEADER_CARDS_CHOICE,
    PRE_GAME_RESOURCES_CHOICE,
    VISIT,
    SELECT_MARBLES,
    PICK_MARBLES,
    BUY_DEVELOPMENT_CARD,
    SELECT_DEVELOPMENT_SLOT,
    SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD,
    DISCARD_LEADER_CARD,
    ACTIVATE_LEADER_CARD,
    SELECT_PRODUCTION_BUTTONS,
    SELECT_PRODUCTION_INPUT_RESOURCES,
    SELECT_PRODUCTION_OUTPUT_RESOURCES;

    /**
     * Acts as selector for the strategy that will be applied.
     */
    public void applyTo(Button confirmButton, Button resetButton) {
        switch (this) {
            case NONE -> setStrategy(confirmButton, resetButton, null, false, false);
            case PRE_GAME_LEADER_CARDS_CHOICE, SELECT_MARBLES -> setStrategy(confirmButton,
                    resetButton, this, true, false);
            case PRE_GAME_RESOURCES_CHOICE, SELECT_DEVELOPMENT_SLOT,
                    SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD, DISCARD_LEADER_CARD,
                    ACTIVATE_LEADER_CARD, SELECT_PRODUCTION_BUTTONS,
                    SELECT_PRODUCTION_INPUT_RESOURCES, SELECT_PRODUCTION_OUTPUT_RESOURCES -> setStrategy(confirmButton, resetButton, this, true, true);
            case VISIT -> setStrategy(confirmButton, resetButton, this, false, true);
            case PICK_MARBLES -> setStrategy(confirmButton, resetButton, this, true, true);
            case BUY_DEVELOPMENT_CARD -> setStrategy(confirmButton, resetButton, this, true, true);
        }
    }

    /**
     * Sets visibility rules and UserData of the passed buttons.
     */
    private void setStrategy(Button confirmButton, Button resetButton,
                             ConfirmResetButtonsStrategy strategy, boolean confirmVisibility,
                             boolean resetVisibility) {
        confirmButton.setUserData(strategy);
        resetButton.setUserData(strategy);
        confirmButton.setVisible(confirmVisibility);
        resetButton.setVisible(resetVisibility);
    }
}
