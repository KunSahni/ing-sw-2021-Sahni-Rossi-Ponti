package it.polimi.ingsw.server.remoteview;

import it.polimi.ingsw.network.servertoclient.renderable.ErrorMessage;
import it.polimi.ingsw.network.servertoclient.renderable.PrivateRenderable;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.network.servertoclient.renderable.updates.LeaderCardsBroadcastUpdate;
import it.polimi.ingsw.network.servertoclient.renderable.updates.PlayerBroadcastUpdate;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PlayerAction;

import java.util.*;
import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

public class RemoteView implements Subscriber<Renderable> {
    /**
     * Utility class created to submit to the Game instance all player's
     * Actions received from Connection instances
     */
    public class NetworkMessageForwarder implements Subscriber<PlayerAction> {
        private Subscription connectionSubscription;

        @Override
        public void onSubscribe(Subscription subscription) {
            connectionSubscription = subscription;
            connectionSubscription.request(1);
        }

        @Override
        public void onNext(PlayerAction item) {
            submissionPublisher.submit(item);
            connectionSubscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("MessageForwarder is not subscribed to connection anymore.");
        }
    }

    private final Map<String, Connection> connectedPlayers;
    private final SubmissionPublisher<PlayerAction> submissionPublisher;
    private Subscription subscription;

    /**
     * Creates a RemoteView Object and subscribes to it the Game
     * instance passed via parameter.
     * @param controller Controller that will subscribe to the RemoteView.
     */
    public RemoteView(Controller controller) {
        connectedPlayers = new HashMap<>();
        submissionPublisher = new SubmissionPublisher<>();
        submissionPublisher.subscribe(controller);
    }

    /**
     * Adds a player to the pool of players connected to the RemoteView.
     * The newly added players will now receive all BroadcastRenderables
     * and its PrivateRenderables via the network.
     * @param nickname game participant.
     * @param connection Connection object from which the player sends
     *                   and receives data to and from the server.
     */
    public void connectPlayer(String nickname, Connection connection) {
        connectedPlayers.put(nickname, connection);
        connection.subscribe(new NetworkMessageForwarder());
    }

    /**
     * Unlinks a player that disconnected from the server, removing him
     * from the connected players list and effectively unsubscribing him
     * from model updates.
     * @param nickname disconnected player.
     */
    public void disconnectPlayer(String nickname) {
        connectedPlayers.remove(nickname);
    }

    /**
     * Sends a generic error generic error message to the Player passed
     * as parameter.
     * @param nickname target Player.
     * @param errorText error text which will reach the Client.
     */
    public void raiseError(String nickname, String errorText) {
        connectedPlayers.get(nickname).send(new ErrorMessage(errorText));
    }


    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    /**
     * Listener for model changes. Forwards all the passed items
     * to each Client's View
     * @param item model update
     */
    @Override
    public void onNext(Renderable item) {
        if(item instanceof PrivateRenderable) {
            Optional<Connection> connection = Optional.ofNullable(connectedPlayers.get(((PrivateRenderable) item).getNickname()));
            connection.ifPresent(c -> c.send(item));
        } else {
            List<String> connectedPlayersNicknames = new ArrayList<>(connectedPlayers.keySet());
            if (item instanceof PlayerBroadcastUpdate) {
                PlayerBroadcastUpdate playerBroadcastUpdate = (PlayerBroadcastUpdate) item;
                connectedPlayersNicknames.remove(playerBroadcastUpdate.getNickname());
            } else if (item instanceof LeaderCardsBroadcastUpdate) {
                LeaderCardsBroadcastUpdate leaderCardsBroadcastUpdate = (LeaderCardsBroadcastUpdate) item;
                connectedPlayersNicknames.remove(leaderCardsBroadcastUpdate.getNickname());
            }
            for (String nickname : connectedPlayersNicknames) {
                connectedPlayers.get(nickname).send(item);
            }
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("RemoteView is not subscribed to model anymore.");
    }
}
