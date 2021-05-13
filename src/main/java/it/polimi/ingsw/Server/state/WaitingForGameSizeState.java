package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;

public class WaitingForGameSizeState extends ConnectionState {

    public WaitingForGameSizeState(Connection connection) {
        super(connection);
    }

    @Override
    public boolean messageAllowed(Message message) {
        return message instanceof CreateLobbyMessage;
    }

    @Override
    public void invalidMessage() {

    }

    @Override
    public void readMessage(Message message) {
        if (((CreateLobbyMessage)message).getSize()<=0 || ((CreateLobbyMessage)message).getSize()>4){
            connection.invalidSize();
        }
        else{
            Lobby.getInstance().setSize(((CreateLobbyMessage)message).getSize());
        }
    }
}
