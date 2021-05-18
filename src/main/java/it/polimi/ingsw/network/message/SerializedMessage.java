package it.polimi.ingsw.network.message;

import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.network.message.messages.Message;

import java.io.Serializable;

/**
 * This class can contain either an action or a message which will be transmitted to the server via the network
 */
public class SerializedMessage implements Serializable {
    public final PlayerAction action;
    public final Message message;

    /**
     * @param action the action which the client wants to send to the server
     */
    public SerializedMessage(PlayerAction action) {
        this.action = action;
        this.message = null;
    }

    /**
     * @param message the message which the client wants to send to the server
     */
    public SerializedMessage(Message message) {
        this.action = null;
        this.message = message;
    }

    public PlayerAction getAction() {
        return action;
    }

    public Message getMessage() {
        return message;
    }
}
