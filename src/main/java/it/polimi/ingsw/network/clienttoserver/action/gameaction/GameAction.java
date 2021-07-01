package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.network.clienttoserver.action.Action;
import it.polimi.ingsw.server.model.Game;

/**
 * Abstract representation of an action generated from the game itself as consequence
 * of a certain game state.
 */
public abstract class GameAction implements Action {
    protected final Game game;

    /**
     * @param game target game which will be subject to modifications.
     */
    public GameAction(Game game) {
        this.game = game;
    }
}
