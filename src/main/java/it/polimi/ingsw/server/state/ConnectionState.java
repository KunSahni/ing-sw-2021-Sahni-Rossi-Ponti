package it.polimi.ingsw.server.state;

import it.polimi.ingsw.server.Connection;

import java.io.Serializable;

public abstract class ConnectionState {

    public ConnectionState() {
    }

    public abstract boolean messageAllowed(Serializable serializable);

    public abstract void invalidMessage(Connection connection);

    public abstract void readMessage(Serializable serializable, Connection connection);
}
