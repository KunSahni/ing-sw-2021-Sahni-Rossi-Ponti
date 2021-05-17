package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;

import java.io.Serializable;

public class WaitingForGameSizeState extends ConnectionState {

    public WaitingForGameSizeState(Connection connection) {
        super(connection);
    }

    @Override
    public boolean messageAllowed(Serializable serializable) {
        return serializable instanceof CreateLobbyMessage;
    }

    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(Serializable serializable) {
        if (((CreateLobbyMessage)serializable).getSize()<=0 || ((CreateLobbyMessage)serializable).getSize()>4){
            connection.invalidSize();
        }
        else{
            Lobby.getInstance().setSize(((CreateLobbyMessage)serializable).getSize());
        }
    }
}
