package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.controller.action.Action;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;

/**
 * This class represents the action of resetting the ActionTokenDeck to its original state and moving the black cross by one space on the SinglePlayerFaithTrack
 */
public class ResetAndMoveAction implements Action {
    private final ActionTokenDeck actionTokenDeck;
    private final SinglePlayerFaithTrack singlePlayerFaithTrack;

    /**
     * @param turn the turn in which this action is happening
     */
    public ResetAndMoveAction(ActionTokenDeck actionTokenDeck, SinglePlayerFaithTrack singlePlayerFaithTrack) {
        this.actionTokenDeck = actionTokenDeck;
        this.singlePlayerFaithTrack = singlePlayerFaithTrack;
    }

    @Override
    public void execute(Game game) {
        singlePlayerFaithTrack.moveBlackCross();
        actionTokenDeck.reset();
    }
}