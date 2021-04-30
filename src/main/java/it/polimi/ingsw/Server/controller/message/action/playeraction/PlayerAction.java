package it.polimi.ingsw.server.controller.message.action.playeraction;

import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.model.Player;

/**
 * Class representing player-initiated actions. Created to
 * implement the setPlayer method used by the RemoteView to
 * set the reference to the Player that initiated the action
 * client-side.
 */
public abstract class PlayerAction implements Action {
    Player player;

    /**
     * Add the reference to the player who initiated the Action.
     * @param player server-side reference to the Player.
     */
    void setPlayer(Player player) {
        this.player = player;
    }
}
