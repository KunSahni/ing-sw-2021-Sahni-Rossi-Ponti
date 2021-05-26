package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;

public class AuthenticationState extends ConnectionState {
    private final Connection connection;

    public AuthenticationState(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    /**
     * control if the message is an instance of AuthenticationMessage
     * @param serializedMessage is the message sent from the client received by the connection
     * @return true if message is an instance of AuthenticationMessage, in contrary case false
     */
    @Override
    public boolean messageAllowed(SerializedMessage serializedMessage) {
        return serializedMessage.getMessage() instanceof AuthenticationMessage;
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
     * @param serializedMessage is the message received from Client
     */
    @Override
    public void readMessage(SerializedMessage serializedMessage) {
        Integer gameID = ((AuthenticationMessage) serializedMessage.getMessage()).getRequestedGameID();
        String nickname = ((AuthenticationMessage) serializedMessage.getMessage()).getNickname();
        if (gameID == -1){
            if (!Lobby.getInstance().checkNicknameAvailability(nickname, connection)){
                connection.unavailableNickname();
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
                        connection.getServer().getController(gameID).connectPlayer(nickname, connection);
                    }
                }
                else{
                    if (connection.getServer().getDormantGames().contains(gameID)){
                        Game game = null;//todo: basta questo per ripristinare il game?
                        try {
                            game = new Game(connection.getServer(), gameID, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.getServer().restoreGame(gameID, game);
                        connection.setState(new PlayingState(connection));
                        connection.getServer().getController(gameID).connectPlayer(nickname, connection);
                    }
                }
            }
        }
    }
}
