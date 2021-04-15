package it.polimi.ingsw.server.model.actiontoken;

import it.polimi.ingsw.server.model.actiontoken.ActionToken;

import java.util.*;

public class ActionTokenDeck {

    private final Stack<ActionToken> currentDeck;

    public ActionTokenDeck() {
        this.currentDeck = new Stack<>();
        reset();
    }

    public List<ActionToken> getFullDeck() {
        return new ArrayList<>(){{
            add(ActionToken.MOVEBYTWO);
            add(ActionToken.MOVEBYTWO);
            add(ActionToken.MOVEANDSHUFFLE);
            add(ActionToken.REMOVEGREEN);
            add(ActionToken.REMOVEPURPLE);
            add(ActionToken.REMOVEYELLOW);
            add(ActionToken.REMOVEBLUE);
        }};
    }
    public Stack<ActionToken> getCurrentDeck() {
        return currentDeck;
    }


    public ActionToken pop() {
        return currentDeck.pop();
    }

    /**
     * reset the deck at the beginning of the game and when MOVEANDSHUFFLE token is taken
     */
    public void reset() {

        currentDeck.clear();
        //place the full deck into current deck
        for (ActionToken actionToken: getFullDeck()) {
            currentDeck.push(actionToken);
        }
        //shuffle the deck
        Collections.shuffle(currentDeck);
    }
}