package it.polimi.ingsw.client.utils.dumbobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This is a dumber version of a regular DevelopmentCardSlot,
 * this class only contains the data stored in a DevelopmentCardSlot but has none of its logic.
 */
public class DumbDevelopmentCardSlot {
    private final Stack<DumbDevelopmentCard> developmentCards;
    private final int slotIndex;

    public DumbDevelopmentCardSlot(int slotIndex) {
        this.developmentCards = new Stack<>();
        this.slotIndex = slotIndex;
    }

    public void updateDevelopmentCards(List<DumbDevelopmentCard> updateDevelopmentCards) {
        this.developmentCards.clear();
        this.developmentCards.addAll(updateDevelopmentCards);
    }

    public List<DumbDevelopmentCard> getDevelopmentCards() {
        return new ArrayList<>(developmentCards);
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}
