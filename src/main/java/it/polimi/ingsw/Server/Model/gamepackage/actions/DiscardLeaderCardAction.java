package it.polimi.ingsw.server.model.gamepackage.actions;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

public class DiscardLeaderCardAction implements Action {
    PersonalBoard board;

    public DiscardLeaderCardAction(PersonalBoard board) {
        this.board = board;
    }

    @Override
    public void performAction() {
    }

    /**
    * @param card the leader card that needs to be discarded
    */
    public void performAction(LeaderCard card) {
        board.discardLeaderCard(card);
    }

}