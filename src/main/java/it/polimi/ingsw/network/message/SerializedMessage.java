package it.polimi.ingsw.network.message;

import it.polimi.ingsw.network.message.messages.Message;

import java.io.Serializable;

//todo: add Serializable on branch main

/**
 * This class can contain either an action or a message which will be transmitted to the server via the network
 */
public class SerializedMessage implements Serializable {
    public final Message message;

    /**
     * @param message the message which the client wants to send to the server
     */
    public SerializedMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
