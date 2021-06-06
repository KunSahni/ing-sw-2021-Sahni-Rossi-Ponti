package it.polimi.ingsw.network.clienttoserver.action;

import it.polimi.ingsw.network.clienttoserver.action.gameaction.GameAction;

/**
 * Interface representing any model-changing action in the game.
 */
public interface Action {

    /**
     * Run the Action and modify the model.
     * @return eventual cascading event
     */
    GameAction execute();
}