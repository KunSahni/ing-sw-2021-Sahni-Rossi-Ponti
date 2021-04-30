package it.polimi.ingsw.server.remoteview;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.renderable.ErrorMessage;
import it.polimi.ingsw.server.model.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.renderable.Renderable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class RemoteView implements Flow.Subscriber<Renderable> {
    /**
     * Utility class created to submit to the Game instance all player's
     * Actions received from Connection instances
     */
    private class NetworkMessageForwarder implements Flow.Subscriber<Action> {
        @Override
        public void onSubscribe(Flow.Subscription subscription) {

        }

        @Override
        public void onNext(Action item) {
            item.setPlayer();
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }

    private final Map<Player, Connection> connectedPlayers;
    private final SubmissionPublisher<Action> submissionPublisher;

    /**
     * Creates a RemoteView Object and subscribes to it the Game
     * instance passed via parameter.
     * @param game Controller that will subscribe to the RemoteView.
     */
    public RemoteView(Game game) {
        connectedPlayers = new HashMap<Player, Connection>();
        submissionPublisher = new SubmissionPublisher<Action>();
        submissionPublisher.subscribe(game);
    }

    /**
     * Adds a player to the pool of players connected to the RemoteView.
     * The newly added players will now receive all BroadcastRenderables
     * and its PrivateRenderables via the network.
     * @param player game participant.
     * @param connection Connection object from which the player sends
     *                   and receives data to and from the server.
     */
    public void addConnectedPlayer(Player player, Connection connection) {
        connectedPlayers.put(player, connection);
        connection.subscribe(new NetworkMessageForwarder());
    }

    /**
     * Unlinks a player that disconnected from the server, removing him
     * from the connected players list and effectively unsubscribing him
     * from model updates.
     * @param player disconnected player.
     */
    public void removeDisconnectedPlayer(Player player) {
        connectedPlayers.remove(player);
    }

    /**
     * Sends a generic error generic error message to the Player passed
     * as parameter.
     * @param player target Player.
     * @param errorText error text which will reach the Client.
     */
    public void raiseError(Player player, String errorText) {
        connectedPlayers.get(player).send(new ErrorMessage(errorText));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    /**
     * Listener for model changes. Forwards all the passed items
     * to each Client's View
     * @param item
     */
    @Override
    public void onNext(Renderable item) {
        if(item instanceof PrivateRenderable) {
            connectedPlayers.get(item.getPlayer()).send(item);
        } else {
            for (Connection connection : connectedPlayers.values()) {
                connection.send(item);
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
