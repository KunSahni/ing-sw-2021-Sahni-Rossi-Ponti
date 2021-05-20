package it.polimi.ingsw.server.model.personalboard;

import java.util.*;

import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;


public class DevelopmentCardSlot implements VictoryPointsElement {
    private final LinkedList<DevelopmentCard> cards;
    private int slotIndex;
    private transient ChangesHandler changesHandler;
    private transient String nickname;

    private DevelopmentCardSlot() {
        this.cards = new LinkedList<>();
    }

    public void init(String nickname, ChangesHandler changesHandler) {
        this.nickname = nickname;
        this.changesHandler = changesHandler;
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
        changesHandler.writeDevelopmentCardSlot(nickname, this);
    }

    public boolean canPlaceCard(DevelopmentCard card) {
        return switch (card.getLevel()) {
            case LEVEL1 -> cards.size() == 0;
            case LEVEL2 -> peek().getLevel().equals(Level.LEVEL1);
            case LEVEL3 -> peek().getLevel().equals(Level.LEVEL2);
        };
    }

    /**
     * Returns a ProductionOutput object created by the topmost production card
     */
    public ProductionOutput produce() {
        assert cards.peek() != null;
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

    public int getSlotIndex() {
        return slotIndex;
    }
}