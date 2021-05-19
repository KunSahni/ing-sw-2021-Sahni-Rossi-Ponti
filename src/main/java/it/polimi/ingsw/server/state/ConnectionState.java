package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.server.Connection;

public abstract class ConnectionState {
    private Connection connection;

    public ConnectionState(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean messageAllowed(SerializedMessage serializedMessage);

    public abstract void invalidMessage();

    public abstract void readMessage(SerializedMessage serializedMessage);
}
