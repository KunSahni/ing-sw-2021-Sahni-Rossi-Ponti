package it.polimi.ingsw.server.controller.gamepackage.turnstates;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.gamepackage.actions.Action;
import it.polimi.ingsw.server.controller.gamepackage.actions.ActivateLeaderCardAction;

public class ActivateLeaderCard implements AbstractTurnState {
    private ActivateLeaderCardAction action;

    @Override
    public void setup(Turn turn, Action action) {
        this.action = (ActivateLeaderCardAction) action;
    }

    /**
     * @param card the leader card that needs to be discarded
     */
    private void activateLeaderCard(LeaderCard card) {
        card.activate();
    }

    @Override
    public void performAction() {
        activateLeaderCard(action.getLeaderCard());
    }
}