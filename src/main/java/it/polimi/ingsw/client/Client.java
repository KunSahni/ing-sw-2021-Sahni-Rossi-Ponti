package it.polimi.ingsw.client;

import it.polimi.ingsw.network.message.SerializedMessage;
import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.network.message.messages.Message;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.network.message.renderable.Renderable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Flow.*;

/**
 * This is the Client of the game which receives and elaborates all the Renderables sent by the Server
 */
public class Client implements Subscriber<Renderable> {
    private Player currentPlayer;
    private final Queue<Renderable> updatesQueue;
    private final UI ui;
    private final ClientSocket clientSocket;
    private Subscription subscription;

    public Client(UI ui) {
        this.ui = ui;
        updatesQueue = new LinkedList<>();
        clientSocket = new ClientSocket("localhost", 8080, this);
    }

    /**
     * This method creates a socket to communicate with the game server
     */
    public void run(){
        clientSocket.connect();
    }

    /**
     * This method closes the socket and safely closes everything so that the client app can be closed
     */
    public void stop(){
        clientSocket.close();
        //todo: implement other features for safe close
    }

    /**
     * This method call render() on the head of updatesQueue and removes it from the queue of updates
     */
    private synchronized void elaborateUpdatesQueue(){
        updatesQueue.remove().render(ui);
    }

    /**
    * @param message a connection related message which will be sent to the server
    */
    public void sendMessage(Message message) {
        clientSocket.sendMessage(new SerializedMessage(message));
    }

    /**
     * @param action an action which the clients wants to execute
     */
    public void sendAction(Action action) {
        clientSocket.sendMessage(new SerializedMessage(action));
    }

    /**
     * Method invoked prior to invoking any other Subscriber
     * methods for the given Subscription. If this method throws
     * an exception, resulting behavior is not guaranteed, but may
     * cause the Subscription not to be established or to be cancelled.
     *
     * <p>Typically, implementations of this method invoke {@code
     * subscription.request} to enable receiving items.
     *
     * @param subscription a new subscription
     */
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    /**
     * Method invoked with a Subscription's next item.  If this
     * method throws an exception, resulting behavior is not
     * guaranteed, but may cause the Subscription to be cancelled.
     *
     * @param item the item
     */
    @Override
    public void onNext(Renderable item) {
        new Thread(() -> {
            updatesQueue.add(item);
            elaborateUpdatesQueue();
        }).start();
        subscription.request(1);
    }

    /**
     * Method invoked upon an unrecoverable error encountered by a
     * Publisher or Subscription, after which no other Subscriber
     * methods are invoked by the Subscription.  If this method
     * itself throws an exception, resulting behavior is
     * undefined.
     *
     * @param throwable the exception
     */
    @Override
    public void onError(Throwable throwable) {

    }

    /**
     * Method invoked when it is known that no additional
     * Subscriber method invocations will occur for a Subscription
     * that is not already terminated by error, after which no
     * other Subscriber methods are invoked by the Subscription.
     * If this method throws an exception, resulting behavior is
     * undefined.
     */
    @Override
    public void onComplete() {

    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private Player getCurrentPlayer() {
        return currentPlayer;
    }

    public UI getUI() {
        return ui;
    }
}