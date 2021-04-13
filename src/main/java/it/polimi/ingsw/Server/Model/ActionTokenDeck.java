package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static it.polimi.ingsw.server.model.ActionToken.*;

public class ActionTokenDeck {

    private Stack<ActionToken> currentDeck = new Stack<ActionToken>();
    private final List<ActionToken> fullDeck = new ArrayList<ActionToken>(){{
        add(ActionToken.MOVEBYTWO);
        add(ActionToken.MOVEBYTWO);
        add(ActionToken.MOVEANDSHUFFLE);
        add(ActionToken.REMOVEGREEN);
        add(ActionToken.REMOVEPURPLE);
        add(ActionToken.REMOVEYELLOW);
        add(ActionToken.REMOVEBLUE);
    }};

    private List<ActionToken> getFullDeck() {
        return fullDeck;
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
        List<ActionToken> sup = new ArrayList<>();
        Random random = ThreadLocalRandom.current();
        int i = 0;

        currentDeck.clear();
        //place the deck into a list
        for (ActionToken actionToken: fullDeck) {
            sup.add(actionToken);
        }
        //shuffle the array
        Collections.shuffle(sup);
        //put the cards in the list in deck
        for (ActionToken actionToken: sup) {
            currentDeck.push(actionToken);
        }
    }
}