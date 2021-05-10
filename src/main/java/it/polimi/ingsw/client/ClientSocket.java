package it.polimi.ingsw.client;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.network.message.renderable.Renderable;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.SubmissionPublisher;

/**
 * This class contains the client-side socket used to communicate with the server
 */
public class ClientSocket {
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private final SubmissionPublisher<Renderable> renderablePublisher;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientSocket(String serverAddress, int serverPort, Client client) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.renderablePublisher = new SubmissionPublisher<>();
        this.renderablePublisher.subscribe(client);
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
                    Renderable message = null;
                    try {
                        message = (Renderable) inputStream.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    renderablePublisher.submit(message);
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
    * @param message a message which the clients wants to send to the server
    */
    public void sendMessage(SerializedMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}