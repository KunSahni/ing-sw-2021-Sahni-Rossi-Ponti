package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.Connection;

public class PlayingState extends ConnectionState{
    public PlayingState(Connection connection) {
        super(connection);
    }

    @Override
    public boolean messageAllowed(Message message) {
        return message instanceof SerializedMessage;
    }

    @Override
    public void invalidMessage() {
    }

    @Override
    public void readMessage(Message message) {
        connection.
    }
}
