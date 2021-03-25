package it.polimi.ingsw.server.model.gamepackage.turnstates;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.gamepackage.Turn;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;
import it.polimi.ingsw.server.model.gamepackage.actions.ActivateLeaderCardAction;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

public class DiscardLeaderCard implements AbstractTurnState {
    private ActivateLeaderCardAction action;
    private PersonalBoard board;

    @Override
    public void setup(Turn turn, Action action) {
        this.action = (ActivateLeaderCardAction) action;
        this.board = turn.getPlayer().getPersonalBoard();
    }

    /**
     * @param card the leader card that needs to be discarded
     */
    private void discardLeaderCard(LeaderCard card) {
        board.discardLeaderCard(card);
    }

    @Override
    public void performAction() {
        discardLeaderCard(action.getLeaderCard());
        board.getFaithTrack().moveMarker();
    }
}