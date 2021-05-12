package it.polimi.ingsw.network.message.messages;

/**
 * This class represents a message sent by the client after the server sends a TimeoutWarningNotification
 */
public class ClientAliveMessage implements Message{
    public ClientAliveMessage() {
        super();
    }
}

//todo: dubbione, questa classe non è superflua?