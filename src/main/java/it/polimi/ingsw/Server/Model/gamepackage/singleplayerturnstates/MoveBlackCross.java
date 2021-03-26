package it.polimi.ingsw.server.model.gamepackage.singleplayerturnstates;

import it.polimi.ingsw.server.model.gamepackage.Turn;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;
import it.polimi.ingsw.server.model.gamepackage.turnstates.AbstractTurnState;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;


public class MoveBlackCross implements AbstractTurnState {
    private SinglePlayerFaithTrack singlePlayerFaithTrack;

    @Override
    public void setup(Turn turn, Action action) {
        this.singlePlayerFaithTrack = (SinglePlayerFaithTrack) turn.getPlayer().getPersonalBoard().getFaithTrack();
    }

    @Override
    public void performAction() {
        singlePlayerFaithTrack.moveBlackCross();
        singlePlayerFaithTrack.moveBlackCross();
    }
}