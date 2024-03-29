package it.polimi.ingsw.server.connection;

import it.polimi.ingsw.network.clienttoserver.SerializedMessage;
import it.polimi.ingsw.network.servertoclient.renderable.ErrorMessage;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.network.servertoclient.renderable.requests.*;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.remoteview.RemoteView;
import it.polimi.ingsw.server.connection.state.AuthenticationState;
import it.polimi.ingsw.server.connection.state.ConnectionState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

public class Connection implements Runnable {
    private final Logger logger;
    private final Socket socket;
    private final Server server;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String nickname;
    private int gameId;
    private boolean isActive;
    private ConnectionState state;
    private final SubmissionPublisher<PlayerAction> submissionPublisher;

    /**
     * Create a connection with the given socket and server,set isActive true and set state to
     * AuthenticationState
     *
     * @param socket is the Client socket
     * @param server is the server from which this method is called
     */
    public Connection(Socket socket, Server server) {
        this.logger = Logger.getLogger(getClass().getSimpleName());
        this.socket = socket;
        this.server = server;
        this.isActive = true;
        this.submissionPublisher = new SubmissionPublisher<>();
        state = new AuthenticationState(this);
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error during initialization of the client!");
            System.err.println(e.getMessage());
        }
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * At the begin call sendAuthenticationRequest method, then hear for communications from view
     * or virtual view
     */
    public void run() {
        try {
            sendAuthenticationRequest();
            while (isActive) {
                readFromInputStream();
            }
        } finally {
            closeConnection();
        }
    }

    public void closeConnection() {
        try {
            if (server.getController(gameId) != null) {
                server.getController(gameId).disconnectPlayer(nickname);
            } else {
                Lobby.getInstance().removePlayer(nickname);
            }
            isActive = false;
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Depending by current state control validity of received messages and invoke corresponding
     * readMessage method
     */
    public synchronized void readFromInputStream() {
        SerializedMessage serializedMessage = null;
        try {
            // logger.info("Waiting to read next client message. Current state: " + state.getClass().getSimpleName());
            serializedMessage = (SerializedMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
        if (serializedMessage != null && serializedMessage.getMessage() != null) {
            if (state.messageAllowed(serializedMessage))
                state.readMessage(serializedMessage);
            else {
                state.invalidMessage();
            }
        }

        if (serializedMessage != null && serializedMessage.getAction() != null) {
            if (state.messageAllowed(serializedMessage))
                state.readMessage(serializedMessage);
            else {
                state.invalidMessage();
            }
        }
    }

    public Server getServer() {
        return server;
    }

    /**
     * send an AuthenticationRequest to the client
     */
    public void sendAuthenticationRequest() {
        send(new AuthenticationRequest());
    }

    /**
     * Send to the client a message to tell that the message he sent is not valid
     */
    public void invalidMessage() {
        send(new ErrorMessage("Your message is not valid"));
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Send to the client a message to tell him that the nickname he chose is unavailable
     */
    public void unavailableNickname() {
        send(new NicknameAlreadyInUseNotification());
        sendAuthenticationRequest();
    }

    /**
     * Send to the client a message to tell him that the size he selected is not valid
     */
    public void invalidSize() {
        send(new ErrorMessage("Game size you selected is invalid"));
    }

    /**
     * Communicate to the player that he is waiting for other players to start game
     */
    public void waitForPlayers() {
        send(new WaitingForPlayersNotification());
    }

    /**
     * Communicate to player that the game he's trying to reconnect is unavailable
     */
    public void unavailableGame() {
        send(new GameNotFoundNotification());
        sendAuthenticationRequest();
    }

    /**
     * Communicate to the player that the game he's trying to reconnect is available but nickname
     * he selected is wrong
     */
    public void wrongNickname() {
        send(new WrongNicknameNotification());
        sendAuthenticationRequest();
    }

    /**
     * Send to the client the passed renderable
     *
     * @param renderable is sent to the Client
     */
    public void send(Renderable renderable) {
        logger.info("Sent " + renderable.getClass().getSimpleName() + " to " + nickname);
        try {
            outputStream.writeObject(renderable);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public void subscribe(RemoteView.NetworkMessageForwarder networkMessageForwarder) {
        this.submissionPublisher.subscribe(networkMessageForwarder);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
        server.addNicknameGameId(this.nickname, this.gameId);
    }

    public void publish(PlayerAction playerAction) {
        submissionPublisher.submit(playerAction);
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }
}
