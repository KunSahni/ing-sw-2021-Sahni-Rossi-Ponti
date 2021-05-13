package it.polimi.ingsw.server.state;

import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.model.Game;

import java.io.FileNotFoundException;

public class AuthenticationState extends ConnectionState {

    public AuthenticationState(Connection connection) {
        super(connection);
    }

    /**
     * control if the message is an instance of AuthenticationMessage
     * @param message is the message sent from the client received by the connection
     * @return true if message is an instance of AuthenticationMessage, in contrary case false
     */
    @Override
    public boolean messageAllowed(Message message) {
        return message instanceof AuthenticationMessage;
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
     * @param message is the message received from Client
     */
    @Override
    public synchronized void readMessage(Message message) {
        if (((AuthenticationMessage) message).getRequestedGameID() == -1){
            if (Lobby.getInstance().getPlayers().contains(((AuthenticationMessage) message).getNickname())){
                connection.unavailableNickname();
            }
            else {
                Lobby.getInstance().addPlayer(((AuthenticationMessage) message).getNickname(), connection);
            }
            if (Lobby.getInstance().isEmpty()){
                connection.askForSize();
            }
            if (Lobby.getInstance().isFull()){
                Lobby.getInstance().startGame();
            }
            else {
                connection.waitForPlayers();
            }
        }

        else {
            if (!connection.getServer().getPlayers().containsValue(((AuthenticationMessage) message).getRequestedGameID()) || !connection.getServer().getDormantGames().contains(((AuthenticationMessage) message).getRequestedGameID())){
                connection.unavailableGame();
            }
            else {
                if (connection.getServer().getPlayers().containsValue(((AuthenticationMessage) message).getRequestedGameID())){
                    if (!connection.getServer().getPlayers().containsKey(((AuthenticationMessage) message).getNickname())){
                        connection.wrongNickname();
                    }
                    else{
                        connection.getServer().getCurrentGames().get(((AuthenticationMessage) message).getRequestedGameID()).connect(((AuthenticationMessage) message).getNickname());
                    }
                }
                else{
                    if (connection.getServer().getDormantGames().contains(((AuthenticationMessage) message).getRequestedGameID())){
                        Game game = null;//todo: basta questo per ripristinare il game?
                        try {
                            game = new Game(((AuthenticationMessage) message).getRequestedGameID(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        connection.getServer().restoreGame(((AuthenticationMessage) message).getRequestedGameID(), game);
                        connection.getServer().getCurrentGames().get(((AuthenticationMessage) message).getRequestedGameID()).connect(((AuthenticationMessage) message).getNickname());
                    }
                }
            }
        }
    }
}
