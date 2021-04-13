package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;




public class DevelopmentCardsDeck {
    private Stack<DevelopmentCard> deck;


    public DevelopmentCard pop() {
        return deck.pop();
    }

    /**
     * this method shuffle the deck
     * array sup is used to "park" all the cards contained in the deck
     * array b is used to mark if a card has been reinserted in the deck
     */
    private void shuffle() {
        List<DevelopmentCard> sup = new ArrayList<>();
        //place the deck into a list
        for (DevelopmentCard d: deck) {
            sup.add(d);
        }
        //shuffle the array
        Collections.shuffle(sup);
        //put the array in the deck
        for (DevelopmentCard d: sup) {
            deck.push(d);
        }
    }
    /**
     * pushes all the cards in the deck and than shuffle them
    * @param cards contains the cards that are going in the deck
    */


    public DevelopmentCardsDeck(List<DevelopmentCard> cards) {
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