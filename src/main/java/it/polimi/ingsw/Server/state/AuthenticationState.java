package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class AuthenticationState extends ConnectionState {

    public AuthenticationState(Connection connection) {
        super(connection);
    }

    /**
     * control if the message is an instance of AuthenticationMessage
     * @param serializable is the message sent from the client received by the connection
     * @return true if message is an instance of AuthenticationMessage, in contrary case false
     */
    @Override
    public boolean messageAllowed(Serializable serializable) {
        return serializable instanceof AuthenticationMessage;
    }


    @Override
    public void invalidMessage() {

    }

    /**
     * Read the authentication response sent by the client to control if gameId is major 0 the corresponding game
     * still exists, if it exists if it is a dormant game that has to be restored or a playing game and the player is
     * connected to it. If the game Id -1 it checks if a Lobby is already instanced, if it is add the player to it,
     * in contrary case asks to the player which size ha wants for the game (1-4) and instance Lobby with that size.
     * The method eventually control if Lobby is full and in that case start the game.
     * @param serializable is the message received from Client
     */
    @Override
    public synchronized void readMessage(Serializable serializable) {
        if (((AuthenticationMessage) serializable).getRequestedGameID() == -1){
            if (Lobby.getInstance().getPlayers().contains(((AuthenticationMessage) serializable).getNickname())){
                connection.unavailableNickname();
            }
            else {
                Lobby.getInstance().addPlayer(((AuthenticationMessage) serializable).getNickname(), connection);
                connection.setNickname(((AuthenticationMessage) serializable).getNickname());
            }
            if (Lobby.getInstance().isEmpty()){
                connection.askForSize();
            }
            else {
                connection.waitForPlayers();
            }
        }

        else {
            if (!connection.getServer().getPlayers().containsValue(((AuthenticationMessage) serializable).getRequestedGameID()) || !connection.getServer().getDormantGames().contains(((AuthenticationMessage) serializable).getRequestedGameID())){
                connection.unavailableGame();
            }
            else {
                if (connection.getServer().getPlayers().containsValue(((AuthenticationMessage) serializable).getRequestedGameID())){
                    if (!connection.getServer().getPlayers().containsKey(((AuthenticationMessage) serializable).getNickname())){
                        connection.wrongNickname();
                    }
                }
            }
        }
    }
}
