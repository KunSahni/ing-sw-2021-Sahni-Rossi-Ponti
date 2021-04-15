package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;

/**
 * This class represents the action of activating a LeaderCard chosen by a Player
 */
public class ActivateLeaderCardAction implements Forwardable {
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
    public void forward() {
        activateLeaderCard();
    }
}