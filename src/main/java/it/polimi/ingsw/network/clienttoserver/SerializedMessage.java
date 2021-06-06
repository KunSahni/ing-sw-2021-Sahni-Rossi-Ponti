package it.polimi.ingsw.network.clienttoserver;

import it.polimi.ingsw.network.clienttoserver.action.playeraction.PlayerAction;
import it.polimi.ingsw.network.clienttoserver.messages.Message;

import java.io.Serializable;

/**
 * This class can contain either an action or a message which will be transmitted to the server via the network
 */
public class SerializedMessage implements Serializable {
    private final PlayerAction action;
    private final Message message;

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
