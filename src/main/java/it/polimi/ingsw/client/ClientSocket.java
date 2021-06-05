package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

/**
 * This class contains the client-side socket used to communicate with the server
 */
public class ClientSocket {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private final SubmissionPublisher<Renderable> renderablePublisher;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientSocket(String serverAddress, int serverPort, DumbModel.UpdatesHandler updatesHandler) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.renderablePublisher = new SubmissionPublisher<>();
        this.renderablePublisher.subscribe(updatesHandler);
    }

    /**
     * This method creates the socket and creates a Thread which waits for server messages
     */
    public void connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            //A thread which will constantly listen for updates from the server and forward them to Client
            new Thread(() -> {
                while (socket.isConnected()) {
                    try {
                        Renderable message = (Renderable) inputStream.readObject();
                        if(message != null) {
                            logger.info("Received " + message.getClass() + " from server.");
                            renderablePublisher.submit(message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the connection established through the socket and its input/output streams
     */
    public void close(){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message a connection related message which will be sent to the server
     */
    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(new SerializedMessage(message));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Sent message instance of: " + message.getClass());
    }

    /**
     * @param action an action which the clients wants to execute
     */
    public void sendAction(PlayerAction action) {
        try {
            outputStream.writeObject(new SerializedMessage(action));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}