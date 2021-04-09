package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.messages.actions.*;
import it.polimi.ingsw.server.controller.gamestates.LastRound;
import it.polimi.ingsw.server.model.Player;

import java.util.concurrent.Flow.*;

/**
 * This class represents a single Turn in a multiplayer game
 */
public class Turn implements Subscriber<Forwardable> {
    private final Game game;
    private final Player player;
    private Forwardable currentAction;
    private Subscription subscription;

    public Turn(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * This method sets a Forwardable as the nextAction chosen by the Player, it also executes it
    * @param nextAction the next action that the Player wants to do
    */
    public void nextAction(Forwardable nextAction) {
        currentAction = nextAction;

        if(player.getPersonalBoard().getDevelopmentCardsCount() == 7 || player.getPersonalBoard().getFaithTrack().getPosition() == 20)
            triggerLastRound();

        if(!(currentAction instanceof StartAction) && !(currentAction instanceof EndAction)){
            this.currentAction.forward();
            player.addAction(currentAction);
        }else if(currentAction instanceof EndAction)
            game.nextTurn();
    }

    /**
     * This method tells the Game that a Player has either completed the FaithTrack or has bought 7 DevelopmentCards and therefore the last round has just begun
     */
    private void triggerLastRound() {
        game.setNextState(new LastRound(game));
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Forwardable getCurrentAction() {
        return currentAction;
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
        this.subscription.request(1);
    }

    /**
     * Method invoked with a Subscription's next item.  If this
     * method throws an exception, resulting behavior is not
     * guaranteed, but may cause the Subscription to be cancelled.
     *
     * @param item the item
     */
    @Override
    public void onNext(Forwardable item) {
        nextAction(item);
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
        System.out.println("Error occurred: " + throwable.getMessage());
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
        System.out.println("Turn finished");
    }


}