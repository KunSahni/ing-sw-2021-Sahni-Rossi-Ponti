package it.polimi.ingsw.server.state;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;

import java.io.Serializable;

public class PlayingState extends ConnectionState{
    public PlayingState(Connection connection) {
        super(connection);
    }

    @Override
    public boolean messageAllowed(Serializable serializable) {
        return serializable instanceof PlayerAction;
    }

    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(Serializable serializable) {
        connection.publish((PlayerAction) serializable); //todo: how player can disconnect?
    }
}
