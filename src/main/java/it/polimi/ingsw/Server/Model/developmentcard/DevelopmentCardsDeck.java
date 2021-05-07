package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.server.model.utils.ChangesHandler;

import java.io.FileNotFoundException;
import java.util.*;

public class DevelopmentCardsDeck {
    private final LinkedList<DevelopmentCard> deck;
    private final ChangesHandler changesHandler;

    public DevelopmentCardsDeck(ChangesHandler changesHandler, Color color, Level level)
            throws FileNotFoundException {
        this.changesHandler = changesHandler;
        this.deck = new LinkedList<>(changesHandler.readDevelopmentCardsDeck(color, level));
    }

    public DevelopmentCard pop() {
        return deck.pop();
    }

    /**
     *
     * @return the first card of the deck not removing it
     */
    public DevelopmentCard peek() {
        if(deck.isEmpty()){return null;}
        else {return deck.peek();}
    }

    public List<DevelopmentCard> getDeck(){
        return new ArrayList<>(deck);
    }
}