package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;


/**
 * This class represents the action of moving the black cross by two spaces on the SinglePlayerFaithTrack
 */
public class MoveBlackCrossAction implements Forwardable {
    private final SinglePlayerFaithTrack singlePlayerFaithTrack;

    /**
     * @param turn the turn in which this action is happening
     */
    public MoveBlackCrossAction(Turn turn) {
        this.singlePlayerFaithTrack = (SinglePlayerFaithTrack) turn.getPlayer().getPersonalBoard().getFaithTrack();
    }

    @Override
    public void forward() {
        singlePlayerFaithTrack.moveBlackCross();
        singlePlayerFaithTrack.moveBlackCross();
    }
}