package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;

import java.io.Serializable;

public class WaitingForGameSizeState extends ConnectionState {
    private static WaitingForGameSizeState instance;

    public static WaitingForGameSizeState getInstance(){
        if (instance == null){
            instance = new WaitingForGameSizeState();
        }
        return instance;
    }

    private WaitingForGameSizeState() {
        super();
    }

    @Override
    public boolean messageAllowed(Serializable serializable) {
        return serializable instanceof CreateLobbyMessage;
    }

    @Override
    public void invalidMessage(Connection connection) {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(Serializable serializable, Connection connection) {
        if (((CreateLobbyMessage)serializable).getSize()<=0 || ((CreateLobbyMessage)serializable).getSize()>4){
            connection.invalidSize();
        }
        else{
            Lobby.getInstance().setSize(((CreateLobbyMessage)serializable).getSize());
        }
    }
}
