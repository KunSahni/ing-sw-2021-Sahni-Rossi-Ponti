package it.polimi.ingsw.server;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.network.message.renderable.ErrorMessage;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.network.message.renderable.requests.*;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.remoteview.RemoteView;
import it.polimi.ingsw.server.state.AuthenticationState;
import it.polimi.ingsw.server.state.ConnectionState;
import it.polimi.ingsw.server.state.PlayingState;
import it.polimi.ingsw.server.state.WaitingForGameSizeState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Connection implements Runnable{
    private final Socket socket;
    private final Server server;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String nickname;
    private int gameId;
    private boolean isActive;
    private ConnectionState state;
    private final SubmissionPublisher<Renderable> renderablePublisher;

    /**
     * Create a connection with the given socket and server,set isActive true and set state to AuthenticationState
     * @param socket is the Client socket
     * @param server is the server from which this method is called
     */
    public Connection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.isActive = true;
        this.renderablePublisher = new SubmissionPublisher<>();
        state = new AuthenticationState(this);
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error during initialization of the client!");
            System.err.println(e.getMessage());
        }
    }

    /**
     * At the begin call authentication method, then hear for communications from view or virtual view
     */
    public void run(){
        try {
            authentication();

            state = new PlayingState(this);
            while(isActive){
                readFromInputStream();
            }
        } finally {
            closeConnection();
        }
    }

    public synchronized void closeConnection(){
        try{
            isActive = false;
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Ask to the player which size he wants for the game by sending a CreateLobbyRequest
     * The state is changed to WaitingForGameSizeState
     */
    public void askForSize(){
        try {
            outputStream.writeObject(new CreateLobbyRequest());
            outputStream.flush();
            state = new WaitingForGameSizeState(this);
            readFromInputStream();
            state = new AuthenticationState(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Depending by current state control validity of received messages and invoke corresponding readMessage method
     */
    public void readFromInputStream(){
        Message message;
        SerializedMessage serializedMessage = null;
        try {
            serializedMessage = (SerializedMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (serializedMessage.message!=null){
            message = serializedMessage.message;
            boolean allowedFlag = state.messageAllowed(message);
            if (!allowedFlag){
                invalidMessage();
            }
            else{
                state.readMessage(message);
            }
        }
    }

    public Server getServer() {
        return server;
    }

    /**
     * send an AuthenticationRequest to the client
     */
    public void authentication(){
        try {
            outputStream.writeObject(new AuthenticationRequest());
            outputStream.flush();
            readFromInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send to the client a message to tell that the message he sent is not valid
     */
    public void invalidMessage(){
        try {
            outputStream.writeObject(new ErrorMessage("Your message is not valid"));
            outputStream.flush();
            authentication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Send to the client a message to tell him that the nickname he chose is unavailable
     */
    public void unavailableNickname(){
        try {
            outputStream.writeObject(new ErrorMessage("Nickname you selected is unavailable"));
            outputStream.flush();
            authentication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send to the client a message to tell him that the size he selected is not valid
     */
    public void invalidSize(){
        try {
            outputStream.writeObject(new ErrorMessage("Game size you selected is invalid"));
            outputStream.flush();
            askForSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Communicate to the player that he is waiting for other players to start game
     */
    public void waitForPlayers(){
        try {
            outputStream.writeBytes(new WaitingForPlayersNotification().message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        state = new PlayingState(this);
        //todo: emmò? che faccio?
    }

    /**
     * Communicate to player that the game he's trying to reconnect is unavailable
     */
    public void unavailableGame(){
        try {
            outputStream.writeBytes(new GameNotFoundNotification().message);
            outputStream.flush();
            authentication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Communicate to the player that the game he's trying to reconnect is available but nickname he selected is wrong
     */
    public void wrongNickname(){
        try {
            outputStream.writeBytes(new WrongNicknameNotification().message);
            outputStream.flush();
            authentication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send to the client the passed renderable
     * @param broadcastRenderable is sent to the Client
     */
    public void sendMessage(BroadcastRenderable broadcastRenderable){
        try {
            outputStream.writeObject(broadcastRenderable);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(PrivateRenderable privateRenderable){
        try {
            outputStream.writeObject(privateRenderable);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribeRemoteView(RemoteView remoteView){
        this.renderablePublisher.subscribe(remoteView);
    }
}
