package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.clienttoserver.SerializedMessage;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PlayerAction;

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

    /**
     * send a message to the client to notify that he has sent a wrong message
     */
    @Override
    public void invalidMessage() {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    /**
     * extract the action from the message received, set player nickname in the action
     * and publish the action
     * @param serializedMessage is the message sent from the client
     */
    @Override
    public void readMessage(SerializedMessage serializedMessage) {
        logger.info("Reading action from " + connection.getNickname() + ", of type: " + serializedMessage.getAction().getClass());
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
