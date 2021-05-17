package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.model.Game;

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
        connection.invalidMessage();
        connection.sendAuthenticationRequest();
        connection.readFromInputStream();
    }

    /**
     * Read the sendAuthenticationRequest response sent by the client to control if gameId is major 0 the corresponding game
     * still exists, if it exists if it is a dormant game that has to be restored or a playing game and the player is
     * connected to it. If the game Id -1 it checks if a Lobby is already instanced, if it is add the player to it,
     * in contrary case asks to the player which size ha wants for the game (1-4) and instance Lobby with that size.
     * The method eventually control if Lobby is full and in that case start the game.
     * @param serializable is the message received from Client
     */
    @Override
    public synchronized void readMessage(Serializable serializable) {
        Integer gameID = ((AuthenticationMessage) serializable).getRequestedGameID();
        String nickname = ((AuthenticationMessage) serializable).getNickname();
        if (gameID == -1){
            if (Lobby.getInstance().getPlayers().contains(nickname)){
                connection.unavailableNickname();
            }
            else {
                Lobby.getInstance().addPlayer(nickname, connection);
                connection.setNickname(nickname);
                if (Lobby.getInstance().isEmpty()) {
                    connection.setState(new WaitingForGameSizeState(connection));
                    connection.askForSize();
                }

                connection.setState(new PlayingState(connection));

                if (Lobby.getInstance().isFull()) {
                    Lobby.getInstance().startGame();//todo: settare da Lobby in tutte le connections state a playingstate
                } else {
                    connection.waitForPlayers();
                }
            }
        }

        else {
            if (!connection.getServer().getPlayers().containsValue(gameID) || !connection.getServer().getDormantGames().contains(gameID)){
                connection.unavailableGame();
            }
            else {
                if (connection.getServer().getPlayers().containsValue(gameID)){
                    if (!connection.getServer().getPlayers().containsKey(nickname)){
                        connection.wrongNickname();
                        connection.readFromInputStream();
                    }
                    else{
                        connection.getServer().getRemoteView(gameID).addConnectedPlayer(nickname, connection);
                    }
                }
                else{
                    if (connection.getServer().getDormantGames().contains(gameID)){
                        Game game = null;//todo: basta questo per ripristinare il game?
                        try {
                            game = new Game(gameID, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.getServer().restoreGame(gameID, game);
                        connection.setState(new PlayingState(connection));
                        connection.getServer().getRemoteView(gameID).addConnectedPlayer(nickname, connection);
                    }
                }
            }
        }
    }
}
