package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.controller.action.Action;
import it.polimi.ingsw.server.model.Game;

public abstract class GameAction implements Action {
    protected final Game game;

    public GameAction(Game game) {
        this.game = game;
    }
}
