package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.server.Connection;

public abstract class ConnectionState {

    public ConnectionState() {
    }

    public abstract boolean messageAllowed(SerializedMessage serializedMessage);

    public abstract void invalidMessage(Connection connection);

    public abstract void readMessage(SerializedMessage serializedMessage, Connection connection);
}
