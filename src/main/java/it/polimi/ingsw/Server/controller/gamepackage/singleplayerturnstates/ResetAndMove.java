package it.polimi.ingsw.server.controller.gamepackage.singleplayerturnstates;

import it.polimi.ingsw.server.model.ActionTokenDeck;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.gamepackage.actions.Action;
import it.polimi.ingsw.server.controller.gamepackage.turnstates.AbstractTurnState;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;


public class ResetAndMove implements AbstractTurnState {
    private ActionTokenDeck actionTokenDeck;
    private SinglePlayerFaithTrack singlePlayerFaithTrack;

    @Override
    public void setup(Turn turn, Action action) {
        this.actionTokenDeck = turn.getGame().getActionTokenDeck();
        this.singlePlayerFaithTrack = (SinglePlayerFaithTrack) turn.getPlayer().getPersonalBoard().getFaithTrack();
    }

    @Override
    public void performAction() {
        singlePlayerFaithTrack.moveBlackCross();
        actionTokenDeck.reset();
    }
}