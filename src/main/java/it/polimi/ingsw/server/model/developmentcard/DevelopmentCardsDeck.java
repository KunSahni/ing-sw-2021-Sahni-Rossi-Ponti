package it.polimi.ingsw.server.model.developmentcard;
import java.util.*;

/**
 * Deck of Development cards. Can contain up to 4 cards.
 */
public class DevelopmentCardsDeck {
    private final LinkedList<DevelopmentCard> deck;

    private DevelopmentCardsDeck() {
        deck = new LinkedList<>();
    }

    /**
     * Returns the top most card and removes it from the deck.
     */
    public DevelopmentCard pop() {
        return deck.pop();
    }

    /**
     * Returns the top most card without removing it from the deck.
     */
    public DevelopmentCard peek() {
        if(deck.isEmpty()){return null;}
        else {return deck.peek();}
    }

    public List<DevelopmentCard> getDeck(){
        return new ArrayList<>(deck);
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }
}