package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;




public class DevelopmentCardsDeck {
    private Stack<DevelopmentCard> deck = new Stack<DevelopmentCard>();

    private DevelopmentCardsBoard ;

    public Set<DevelopmentCard> getDeck() {
        return deck;
    }

    public DevelopmentCardsBoard get() {
        return ;
    }

    public void set(DevelopmentCardsBoard ) {
        this. = ;
    }

    public void linkDeck(DevelopmentCard _deck) {
        if (_deck != null) {
            _deck.unlink();
            _deck.set(this);
            getDeck().add(_deck);
        }
    }

    public void link(DevelopmentCardsBoard _) {
        if (_ != null) {
            _.getBoard[4][3]().add(this);
        }

        unlink();
        set(_);
    }

    public void unlinkDeck(DevelopmentCard _deck) {
        if (_deck != null) {
            _deck.set(null);
            getDeck().remove(_deck);
        }
    }

    public void unlinkDeck(DevelopmentCard _deck, Iterator<DevelopmentCard> it) {
        if (_deck != null) {
            _deck.set(null);
            it.remove();
        }
    }

    public void unlink() {
        if (get() != null) {
            get().getBoard[4][3]().remove(this);
            set(null);
        }
    }


    public DevelopmentCard pop() {
        return deck.pop();
    }

    /**
     * this method shuffle the deck
     * array sup is used to "park" all the cards contained in the deck
     * array b is used to mark if a card has been reinserted in the deck
     */
    private void shuffle() {
        DevelopmentCard sup[] = new DevelopmentCard()[4];
        int i = new Integer();
        int rand = new Integer();
        boolean b[] = new Boolean()[4];

        for(i=0; i<4; i++){
            sup[i]= deck.pop();
            b[i]=false;
        }

        for(i=0; i<4; i++){
            rand = random.nextInt(3);
            while(b[rand]==false){
                rand++;
                if (rand>3) rand=0;
            }
            deck.push(sup[rand]);
            b[rand]=true;
        }
    }
    /**
     * pushes all the cards in the deck and than shuffle them
    * @param cards contains the cards that are going in the deck
    */


    public DevelopmentCardsDeck(List<DevelopmentCard> cards) {
        deck.push(cards.get(0));
        deck.push(cards.get(1));
        deck.push(cards.get(2));
        deck.push(cards.get(3));
        shuffle();
    }

    /**
     *
     * @return the first card of the deck not removing it
     */
    public DevelopmentCard peek() {
        return deck.peek();
    }

}