package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;

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
    public boolean messageAllowed(SerializedMessage serializedMessage) {
        return serializedMessage.getMessage() instanceof CreateLobbyMessage;
    }

    @Override
    public void invalidMessage(Connection connection) {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(SerializedMessage serializedMessage, Connection connection) {
        if (((CreateLobbyMessage) serializedMessage.getMessage()).getSize()<=0 || ((CreateLobbyMessage) serializedMessage.getMessage()).getSize()>4){
            connection.invalidSize();
        }
        else{
            Lobby.getInstance().setSize(((CreateLobbyMessage) serializedMessage.getMessage()).getSize());
        }
    }
}
