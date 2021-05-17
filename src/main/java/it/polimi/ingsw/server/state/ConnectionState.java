package it.polimi.ingsw.server.state;

import it.polimi.ingsw.server.Connection;

import java.io.Serializable;

public abstract class ConnectionState {
    Connection connection;

    public ConnectionState(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean messageAllowed(Serializable serializable);

    public abstract void invalidMessage();

    public abstract void readMessage(Serializable serializable);
}
