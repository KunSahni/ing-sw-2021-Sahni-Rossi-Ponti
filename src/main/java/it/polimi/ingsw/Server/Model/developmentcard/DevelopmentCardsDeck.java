package it.polimi.ingsw.server.model.developmentcard;

import java.util.*;




public class DevelopmentCardsDeck {
    private final Stack<DevelopmentCard> deck;


    public DevelopmentCard pop() {
        return deck.pop();
    }


    /**
     * pushes all the cards in the deck and than shuffle them
    * @param cards contains the cards that are going in the deck
    */

    public DevelopmentCardsDeck(List<DevelopmentCard> cards) {
        deck = new Stack<>();
        for (DevelopmentCard d: cards) {
            deck.push(d);
        }
        Collections.shuffle(deck);
    }

    /**
     *
     * @return the first card of the deck not removing it
     */
    public DevelopmentCard peek() {

        if(deck.empty()){return null;}
        else {return deck.peek();}
    }

    public List<DevelopmentCard> getDeck(){
        return new ArrayList<>(deck);
    }
}