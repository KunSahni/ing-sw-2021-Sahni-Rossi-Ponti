package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;

import java.util.*;




public class DevelopmentCardsDeck {
    private final Stack<DevelopmentCard> deck;


    public DevelopmentCard pop() {
        return deck.pop();
    }

    /**
     * this method shuffle the deck
     * array sup is used to "park" all the cards contained in the deck
     * array b is used to mark if a card has been reinserted in the deck
     */
    private void shuffle() {
        Collections.shuffle(deck);
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
        shuffle();
    }

    /**
     *
     * @return the first card of the deck not removing it
     */
    public DevelopmentCard peek() {

        if(deck.empty()){return null;}
        else {return deck.peek();}
    }

}