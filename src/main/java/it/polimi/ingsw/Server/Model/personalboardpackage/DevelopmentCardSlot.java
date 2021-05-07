package it.polimi.ingsw.server.model.personalboardpackage;

import java.io.FileNotFoundException;
import java.util.*;

import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.utils.ChangesHandler;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;


public class DevelopmentCardSlot implements VictoryPointsElement {
    private final LinkedList<DevelopmentCard> cards;
    private final int slotIndex;
    private final ChangesHandler changesHandler;
    private final String nickname;

    public DevelopmentCardSlot(ChangesHandler changesHandler, String nickname, int slotIndex)
            throws FileNotFoundException {
        this.changesHandler = changesHandler;
        this.nickname = nickname;
        this.slotIndex = slotIndex;
        this.cards = new LinkedList<>(changesHandler.readPlayerDevelopmentCardSlot(nickname, slotIndex));
    }

    /**
     * Returns a list representing the current DevelopmentCards stack
     */
    public List<DevelopmentCard> getDevelopmentCards() {
        return new ArrayList<>(cards);
    }

    /**
     * @return reference to the card currently at the top in the DevelopmentSlot
     */
    public DevelopmentCard peek() {
        return cards.peek();
    }

    /**
     *
     * @param card gets added at the top of the stack of cards, becoming the
     *             active card that will be used in productions
     */
    public void placeCard(DevelopmentCard card) {
        cards.addFirst(card);
    }

    /**
     * Returns a ProductionOutput object created by the topmost production card
     */
    public ProductionOutput produce() {
        return cards.peek().produce();
    }

    /**
     * Returns the sum of victory points of all stacked development cards
     */
    @Override
    public int getVictoryPoints() {
        return cards.stream()
                .mapToInt(DevelopmentCard::getVictoryPoints)
                .sum();
    }

    /**
     * Returns the number of cards stacked in the current DevelopmentCardSlot
     */
    public int getCardsNumber() {
        return cards.size();
    }
}