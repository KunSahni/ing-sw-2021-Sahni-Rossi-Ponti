package it.polimi.ingsw.server.model.leadercard;

import java.util.*;
import it.polimi.ingsw.server.model.ChangesHandler;

/**
 * Deck of leader cards from which each player will draw 4 cards
 * at the beginning of a match.
 */
public class LeaderCardsDeck {
    private final LinkedList<LeaderCard> deck;
    private final ChangesHandler changesHandler;

    public LeaderCardsDeck(List<LeaderCard> cards, ChangesHandler changesHandler) {
        this.deck = new LinkedList<>(cards);
        this.changesHandler = changesHandler;
    }

    public List<LeaderCard> getDeck() {
        return new ArrayList<>(deck);
    }


    /**
     * creates and returns the deck for a player popping four cards from the main deck
     * @return supp that contains the deck of a player
     */
    public List<LeaderCard> popFour() {
        List<LeaderCard> supp = new ArrayList<>();

        for(int i=0; i<4; i++){
            supp.add(deck.pop());
        }
        changesHandler.writeLeaderCardsDeck(this);
        return supp;
    }

    public void shuffle() {
        Collections.shuffle(deck);
        changesHandler.writeLeaderCardsDeck(this);
    }
}