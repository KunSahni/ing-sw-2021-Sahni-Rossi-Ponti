package it.polimi.ingsw.server.state;

import it.polimi.ingsw.server.Connection;

import java.io.Serializable;

public class PlayingState extends ConnectionState{
    public PlayingState(Connection connection) {
        super(connection);
    }

    @Override
    public boolean messageAllowed(Serializable serializable) {
        return true;
    }

    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(Serializable serializable) {
    }
}
