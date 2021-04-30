package it.polimi.ingsw.server.controller.message.action.playeraction;

import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;

/**
 * This class represents the action of activating a LeaderCard chosen by a Player
 */
public class ActivateLeaderCardAction implements Action {
    private final LeaderCard leaderCard;

    /**
     * @param leaderCard the leader card that needs to be activated
     */
    public ActivateLeaderCardAction(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    private void activateLeaderCard() {
        leaderCard.activate();
    }

    @Override
    public void execute() {
        activateLeaderCard();
    }
}