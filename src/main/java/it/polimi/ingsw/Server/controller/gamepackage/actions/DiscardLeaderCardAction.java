package it.polimi.ingsw.server.controller.gamepackage.actions;

import it.polimi.ingsw.server.model.LeaderCard;
public class DiscardLeaderCardAction implements Action {
    private final LeaderCard leaderCard;

    public DiscardLeaderCardAction(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }
}