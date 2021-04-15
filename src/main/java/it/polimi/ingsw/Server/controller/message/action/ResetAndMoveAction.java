package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;

/**
 * This class represents the action of resetting the ActionTokenDeck to its original state and moving the black cross by one space on the SinglePlayerFaithTrack
 */
public class ResetAndMoveAction implements Forwardable {
    private final ActionTokenDeck actionTokenDeck;
    private final SinglePlayerFaithTrack singlePlayerFaithTrack;

    /**
     * @param turn the turn in which this action is happening
     */
    public ResetAndMoveAction(Turn turn) {
        this.actionTokenDeck = turn.getGame().getActionTokenDeck();
        this.singlePlayerFaithTrack = (SinglePlayerFaithTrack) turn.getPlayer().getPersonalBoard().getFaithTrack();
    }

    @Override
    public void forward() {
        singlePlayerFaithTrack.moveBlackCross();
        actionTokenDeck.reset();
    }
}