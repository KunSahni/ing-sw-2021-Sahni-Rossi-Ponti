package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.Lobby;

public class WaitingForGameSizeState extends ConnectionState {
    private final Connection connection;

    public WaitingForGameSizeState(Connection connection) {
        super(connection);
        this.connection = connection;
        this.connection.askForSize();
    }

    @Override
    public boolean messageAllowed(SerializedMessage serializedMessage) {
        return serializedMessage.getMessage() instanceof CreateLobbyMessage;
    }

    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(SerializedMessage serializedMessage) {
        System.out.println("Reading lobby size from lobby leader.");
        int selectedSize = ((CreateLobbyMessage) serializedMessage.getMessage()).getSize();
        if (selectedSize <=0 || selectedSize >4){
            connection.invalidSize();
        }
        else{
            Lobby.getInstance().setSize(selectedSize);
        }
    }
}
