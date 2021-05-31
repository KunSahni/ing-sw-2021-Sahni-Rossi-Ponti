package it.polimi.ingsw.server.model.actiontoken;

import it.polimi.ingsw.server.model.ChangesHandler;

import java.util.*;

public class ActionTokenDeck {
    private final Stack<ActionToken> currentDeck;
    private transient ChangesHandler changesHandler;
    private transient List<ActionToken> fullDeck;

    private ActionTokenDeck() {
        this.currentDeck = new Stack<>();
    }

    public void init(ChangesHandler changesHandler) {
        this.changesHandler = changesHandler;
        this.fullDeck = new ArrayList<>(currentDeck);

    }

    /**
     * Returns a List containing the deck of ActionTokens specified in a resource file.
     * @return ActionToken list without a meaningful order.
     */
    public List<ActionToken> getFullDeck() {
        return new ArrayList<>(fullDeck);
    }

    /**
     * Returns a copy of the current ActionTokenDeck state. ActionTokens are in reverse
     * order compared to the stack pop() calls: the ActionToken in the List's last position
     * is the first to get picked on a pop() call on the deck.
     * @return ordered List of ActionTokens representing the current deck state.
     */
    public List<ActionToken> getCurrentDeck() {
        return new ArrayList<>(currentDeck);
    }

    /**
     * Removes the ActionToken at the top of the deck (Stack) and returns it.
     * @return top-most ActionToken, removed from deck.
     */
    public ActionToken pop() {
        ActionToken actionToken = currentDeck.pop();
        changesHandler.writeActionTokenDeck(this);
        return actionToken;
    }


    //todo: is this needed?
    /**
     * Clears the current deck and re-initializes it. ActionTokens
     * will be loaded in a random order.
     */
    public void reset() {
        currentDeck.clear();
        //place the full deck into current deck
        for (ActionToken actionToken: getFullDeck()) {
            currentDeck.push(actionToken);
        }
        //shuffle the deck
        shuffle();
        changesHandler.writeActionTokenDeck(this);
    }


    public void shuffle() {
        Collections.shuffle(currentDeck);
    }
}