package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.Connection;

public abstract class ConnectionState {
    Connection connection;

    public ConnectionState(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean messageAllowed(Message message);

    public abstract void invalidMessage();

    public abstract void readMessage(Message message);
}
