package it.polimi.ingsw.server.model;

import java.util.*;

public class ActionTokenDeck {

    private final Stack<ActionToken> currentDeck = new Stack<>();
    private final List<ActionToken> fullDeck = new ArrayList<>(){{
        add(ActionToken.MOVEBYTWO);
        add(ActionToken.MOVEBYTWO);
        add(ActionToken.MOVEANDSHUFFLE);
        add(ActionToken.REMOVEGREEN);
        add(ActionToken.REMOVEPURPLE);
        add(ActionToken.REMOVEYELLOW);
        add(ActionToken.REMOVEBLUE);
    }};

//    private List<ActionToken> getFullDeck() {
//        return fullDeck;
//    }
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
        for (ActionToken actionToken: fullDeck) {
            currentDeck.push(actionToken);
        }
        //shuffle the deck
        Collections.shuffle(currentDeck);
    }
}