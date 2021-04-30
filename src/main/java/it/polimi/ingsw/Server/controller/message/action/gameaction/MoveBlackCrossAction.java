package it.polimi.ingsw.server.controller.message.action.gameaction;

import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;


/**
 * This class represents the action of moving the black cross by two spaces on the SinglePlayerFaithTrack
 */
public class MoveBlackCrossAction implements Action {
    private final SinglePlayerFaithTrack singlePlayerFaithTrack;

    /**
     * @param turn the turn in which this action is happening
     */
    public MoveBlackCrossAction(SinglePlayerFaithTrack faithTrack) {
        this.singlePlayerFaithTrack = faithTrack;
    }

    @Override
    public void execute() {
        singlePlayerFaithTrack.moveBlackCross();
        singlePlayerFaithTrack.moveBlackCross();
    }
}