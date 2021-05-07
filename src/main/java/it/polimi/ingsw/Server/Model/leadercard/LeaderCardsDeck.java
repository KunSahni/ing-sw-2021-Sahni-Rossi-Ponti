package it.polimi.ingsw.server.model.leadercard;

import java.io.*;
import java.util.*;
import it.polimi.ingsw.server.model.utils.ChangesHandler;


public class LeaderCardsDeck {
    private final LinkedList<LeaderCard> deck;
    private final ChangesHandler changesHandler;

    public LeaderCardsDeck(ChangesHandler changesHandler) throws FileNotFoundException {
        this.changesHandler = changesHandler;
        this.deck = new LinkedList<>(changesHandler.readLeaderCardsDeck());
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
        changesHandler.writeLeaderCardsDeck(deck);
        return supp;
    }
}