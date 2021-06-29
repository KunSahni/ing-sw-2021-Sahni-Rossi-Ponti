package it.polimi.ingsw.server.connection.state;

import it.polimi.ingsw.network.clienttoserver.SerializedMessage;
import it.polimi.ingsw.network.clienttoserver.messages.AuthenticationMessage;
import it.polimi.ingsw.network.servertoclient.renderable.requests.CreateLobbyRequest;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthenticationState extends ConnectionState {
    private final Logger logger = Logger.getLogger(getClass().getName());
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
        AuthenticationMessage authenticationMessage = (AuthenticationMessage) serializedMessage.getMessage();
        Integer gameID = authenticationMessage.getRequestedGameID();
        String nickname = authenticationMessage.getNickname();
        connection.setNickname(nickname);
        logger.info("Player " + nickname + " is trying to join game " + gameID);
        if (gameID == -1){
            if (Lobby.getInstance().isEmpty()) {
                connection.setState(new WaitingForGameSizeState(connection));
                connection.send(new CreateLobbyRequest());
            } else {
                if (!Lobby.getInstance().nicknameAvailable(nickname)){
                    connection.unavailableNickname();
                } else {
                    Lobby.getInstance().addPlayer(nickname, connection);
                }
            }
        } else {
            if (!connection.getServer().getPlayers().containsValue(gameID) && !connection.getServer().getDormantGames().contains(gameID)){
                connection.unavailableGame();
            }
            else {
                if (connection.getServer().getPlayers().containsValue(gameID)){
                    if (!connection.getServer().getPlayers().containsKey(nickname)){
                        connection.wrongNickname();
                        connection.readFromInputStream();
                    }
                    else{
                        if (connection.getServer().playerAlreadyConnected(nickname, gameID)){
                            connection.wrongNickname();
                            connection.readFromInputStream();
                        }
                        else {
                            Lobby.getInstance().setSize(0);
                            connection.setState(new PlayingState(connection));
                            connection.setGameId(gameID);
                            connection.getServer().getController(gameID).connectPlayer(nickname, connection);
                        }
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
                        Controller controller = new Controller(game);
                        RemoteView remoteView = new RemoteView(controller);
                        controller.setRemoteView(remoteView);
                        connection.getServer().restoreGame(gameID, game);
                        connection.getServer().addGameIdController(gameID, controller);
                        connection.setState(new PlayingState(connection));
                        connection.getServer().getController(gameID).connectPlayer(nickname, connection);
                        connection.setGameId(gameID);
                    }
                }
            }
        }
    }
}
