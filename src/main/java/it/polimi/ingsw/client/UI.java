package it.polimi.ingsw.client;

import it.polimi.ingsw.client.utils.dumbobjects.DumbActionTokenDeck;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public interface UI extends Subscriber<Renderable> {

    /**
     * All render methods have the goal of displaying on screen the element in the method name
     */

    void renderPersonalBoard(String nickname);
    void renderCommons();   //Commons stands for market + development cards board
    void renderActionTokenDeck(DumbActionTokenDeck actionTokenDeck);
    void renderGameOutcome(int finalScore);
    void renderGameOutcome(TreeMap<Integer, String> finalScores);
    void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards);
    void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles);
    void renderResourcePregameChoice();
    void renderNotification(String message);
    void renderErrorMessage(String message);
    void renderAuthenticationRequest(String message);
    void renderCreateLobbyRequest(String message);

    DumbModel getDumbModel();
    OnScreenElement getOnScreenElement();

    void setSubscription(Subscription subscription);
    Subscription getSubscription();

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
    default void onSubscribe(Subscription subscription) {
        setSubscription(subscription);
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
    default void onNext(Renderable item) {
        if(getOnScreenElement().equals(item.getOnScreenElement(getDumbModel())) || item.getOnScreenElement(getDumbModel()).equals(OnScreenElement.FORCE_DISPLAY))
            item.render(this);
        getSubscription().request(1);
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
    default void onError(Throwable throwable) {
        throwable.printStackTrace();
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
    default void onComplete() {

    }
}
