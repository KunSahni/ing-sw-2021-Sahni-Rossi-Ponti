package it.polimi.ingsw.server.model.gamepackage.actions;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

public class ActivateLeaderCardAction implements Action {
    PersonalBoard board;

    public ActivateLeaderCardAction(PersonalBoard board) {
        this.board = board;
    }

    @Override
    public void performAction() {
    }

    /**
     * @param card the leader card that needs to be discarded
     */
    public void performAction(LeaderCard card) {
        if(board.getLeaderCard1().equals(card))
            board.getLeaderCard1().activate();
        else if(board.getLeaderCard2().equals(card))
            board.getLeaderCard2().activate();
    }

}