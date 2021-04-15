package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.controller.message.action.Actions;

import java.util.List;

//todo: move the logic behind this from Player to ModelView

/**
 * This class contains a list of possible Actions from which the Player needs to pick one
 */
public class NextActionMessage implements Message{
    private final List<Actions> nextActions;

    public NextActionMessage(List<Actions> nextActions) {
        this.nextActions = nextActions;
    }

    public List<Actions> getNextActions() {
        return nextActions;
    }
}
