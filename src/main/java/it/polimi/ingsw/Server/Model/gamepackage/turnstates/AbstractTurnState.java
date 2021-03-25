package it.polimi.ingsw.server.model.gamepackage.turnstates;

import it.polimi.ingsw.server.model.gamepackage.Turn;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;

import java.util.*;
import java.time.*;

public interface AbstractTurnState {
    public void setup(Turn turn, Action action);
    public void performAction();
}