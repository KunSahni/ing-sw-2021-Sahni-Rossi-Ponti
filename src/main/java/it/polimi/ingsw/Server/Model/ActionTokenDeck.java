package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static it.polimi.ingsw.server.model.ActionToken.*;

public class ActionTokenDeck {

    private Stack<ActionToken> currentDeck = new Stack<ActionToken>();
    private final List<ActionToken> fullDeck = new ArrayList<ActionToken>(){{
        add(MOVEBYTWO);
        add(MOVEBYTWO);
        add(MOVEANDSHUFFLE);
        add(REMOVEGREEN);
        add(REMOVEPURPLE);
        add(REMOVEYELLOW);
        add(REMOVEBLUE);
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
        ActionToken[] sup = new ActionToken[7];
        Random random = ThreadLocalRandom.current();
        int i = 0;

        currentDeck.clear();
        //place the deck into an array
        for (ActionToken actionToken: fullDeck) {
            sup[i] = actionToken;
            i++;
        }
        //shuffle the array
        for(int b=sup.length-1; b>0; b-- ){
            int index = random.nextInt(b+1);
            ActionToken a = sup[index];
            sup[index] = sup[b];
            sup[b] = a;
        }
        //put the array in deck
        for (ActionToken actionToken: sup) {
            currentDeck.push(actionToken);
        }
    }
}