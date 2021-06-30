package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.network.clienttoserver.SerializedMessage;
import it.polimi.ingsw.network.clienttoserver.messages.Message;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PlayerAction;
import it.polimi.ingsw.network.servertoclient.renderable.updates.ServerOfflineUpdate;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

/**
 * This class contains the client-side socket used to communicate with the server
 */
public class ClientSocket {
    private final Logger logger;
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
        this.logger = Logger.getLogger(getClass().getSimpleName());
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
                            //logger.info("Received " + message.getClass().getSimpleName() + " from server.");
                            renderablePublisher.submit(message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        renderablePublisher.submit(new ServerOfflineUpdate());
                        break;
                    }
                }
            }).start();
        } catch (IOException e) {
            renderablePublisher.submit(new ServerOfflineUpdate());
        }
    }

    /**
     * This method closes the connection established through the socket and its input/output streams
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            renderablePublisher.submit(new ServerOfflineUpdate());
        }
    }

    /**
     * @param message a connection related message which will be sent to the server
     */
    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(new SerializedMessage(message));
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // logger.info("Sent message instance of: " + message.getClass().getSimpleName());
    }

    /**
     * @param action an action which the clients wants to execute
     */
    public void sendAction(PlayerAction action) {
        try {
            outputStream.writeObject(new SerializedMessage(action));
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // logger.info("Sent action instance of: " + action.getClass().getSimpleName());
    }
}