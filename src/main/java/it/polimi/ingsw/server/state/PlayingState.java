package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;

public class PlayingState extends ConnectionState{
    private final Connection connection;

    public PlayingState(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public boolean messageAllowed(SerializedMessage serializedMessage) {
        return serializedMessage.getAction() != null; //|| serializedMessage.getMessage() instanceof disconnectMessage;
    }

    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(SerializedMessage serializedMessage) {
        if (serializedMessage.getAction()!=null){
            PlayerAction playerAction = serializedMessage.getAction();
            playerAction.setNickname(connection.getNickname());
            connection.publish(playerAction);
        }
        else{
            connection.closeConnection();
        }
    }
}
