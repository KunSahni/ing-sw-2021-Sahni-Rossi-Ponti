package it.polimi.ingsw.network.message;

import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.controller.message.choice.Message;

import java.io.Serializable;

/**
 * This class can contain either an action or a message which will be transmitted to the server via the network
 */
public class SerializedMessage {
    public final Action action;
    public final Message message;

    /**
     * @param action the action which the client wants to send to the server
     */
    public SerializedMessage(Action action) {
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

    public Action getAction() {
        return action;
    }

    public Message getMessage() {
        return message;
    }
}
