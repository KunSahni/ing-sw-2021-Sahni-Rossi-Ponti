package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

/**
 * This class represents the action of discarding a LeaderCard chosen by a Player
 */
public class DiscardLeaderCardAction implements Forwardable {
    private final PersonalBoard board;
    private final LeaderCard leaderCard;

    /**
     * @param board the Player's PersonalBoard
     * @param leaderCard the leader card that needs to be activated
     */
    public DiscardLeaderCardAction(PersonalBoard board, LeaderCard leaderCard) {
        this.board = board;
        this.leaderCard = leaderCard;
    }

    /**
     * @param card the leader card that needs to be discarded
     */
    private void discardLeaderCard() {
        board.discardLeaderCard(leaderCard);
    }

    @Override
    public void forward() {
        discardLeaderCard();
        board.getFaithTrack().moveMarker();
    }
}