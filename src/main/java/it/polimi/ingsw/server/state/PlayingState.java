package it.polimi.ingsw.server.state;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;

import java.io.Serializable;

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
    public boolean messageAllowed(Serializable serializable) {
        return serializable instanceof PlayerAction;
    }

    @Override
    public void invalidMessage(Connection connection) {
        connection.invalidMessage();
        connection.readFromInputStream();
    }

    @Override
    public void readMessage(Serializable serializable, Connection connection) {
        PlayerAction playerAction = (PlayerAction) serializable;
        playerAction.setNickname(connection.getNickname());
        connection.publish(playerAction); //todo: how player can disconnect?
    }
}
