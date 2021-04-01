package it.polimi.ingsw.server.controller.gamepackage.turnstates;

import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.gamepackage.actions.Action;

public interface AbstractTurnState {
    public void setup(Turn turn, Action action);
    public void performAction();
}