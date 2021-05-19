package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;

public class PlayingState extends ConnectionState{
    private static PlayingState instance;

    public static PlayingState getInstance(){
        if (instance == null){
            instance = new PlayingState();
        }
        return instance;
    }
    private PlayingState() {
        super();
    }

    @Override
    public boolean messageAllowed(SerializedMessage serializedMessage) {
        return serializedMessage.getAction() instanceof PlayerAction;
    }

    @Override
    public void invalidMessage(Connection connection) {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(SerializedMessage serializedMessage, Connection connection) {
        PlayerAction playerAction = serializedMessage.getAction();
        playerAction.setNickname(connection.getNickname());
        connection.publish(playerAction); //todo: how player can disconnect?
    }
}
