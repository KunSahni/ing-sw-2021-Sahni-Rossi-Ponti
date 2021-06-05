package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;

import java.util.logging.Logger;

public class PlayingState extends ConnectionState{
    private final Logger logger = Logger.getLogger(getClass().getName());
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
        logger.info("Reading action");
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
