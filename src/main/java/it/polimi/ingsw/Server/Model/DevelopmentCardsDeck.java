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
        DevelopmentCard[] sup = new DevelopmentCard[4];
        Random random = ThreadLocalRandom.current();
        int i = 0;
        //place the deck into an array
        for (DevelopmentCard d: deck) {
            sup[i] = d;
            i++;
        }
        //shuffle the array
        for(int b=sup.length-1; b>0; b-- ){
            int index = random.nextInt(b+1);
            DevelopmentCard a = sup[index];
            sup[index] = sup[b];
            sup[b] = a;
        }
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