package it.polimi.ingsw.server.model.developmentcard;
import java.util.*;

public class DevelopmentCardsDeck {
    private final LinkedList<DevelopmentCard> deck;

    public DevelopmentCardsDeck() {
        deck = new LinkedList<>();
    }

    public DevelopmentCard pop() {
        return deck.pop();
    }

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