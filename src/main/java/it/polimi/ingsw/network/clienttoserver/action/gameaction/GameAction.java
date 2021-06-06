package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.network.clienttoserver.action.Action;
import it.polimi.ingsw.server.model.Game;

public abstract class GameAction implements Action {
    protected final Game game;

    public GameAction(Game game) {
        this.game = game;
    }
}
