package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.clienttoserver.SerializedMessage;
import it.polimi.ingsw.server.connection.Connection;

public abstract class ConnectionState {
    private Connection connection;

    public ConnectionState(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean messageAllowed(SerializedMessage serializedMessage);

    public abstract void invalidMessage();

    public abstract void readMessage(SerializedMessage serializedMessage);
}
