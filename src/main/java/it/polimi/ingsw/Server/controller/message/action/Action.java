package it.polimi.ingsw.server.controller.message.action;

import java.io.Serializable;

/**
 * Interface representing any model-changing action in the game.
 */
public interface Action extends Serializable {

    /**
     * Run the Action and modify the model.
     */
    void execute();

    /**
     * Check if the the Action is allowed by the current
     * state of the model.
     * @return true if allowed, false otherwise.
     */
    boolean isAllowed();
}