package it.polimi.ingsw.server.controller.action;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;

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