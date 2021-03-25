package it.polimi.ingsw.server.model.gamepackage.actions;

import it.polimi.ingsw.server.model.LeaderCard;

public class ActivateLeaderCardAction implements Action {
    private final LeaderCard leaderCard;

    public ActivateLeaderCardAction(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    public LeaderCard getLeaderCard() {
        return leaderCard;
    }
}