package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.*;


public class DevelopmentCardSlot implements VictoryPointsElement{
    private final Stack<DevelopmentCard> cards;

    public DevelopmentCardSlot() {
        cards = new Stack<>();
    }

    public List<DevelopmentCard> viewStack() {
        return (List<DevelopmentCard>) cards.clone();
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
        cards.addElement(card);
    }

    /**
     * Returns the resources that can be created by the topmost production card
     */
    public Map<Resource, Integer> produce() {
        return ResourceBank.getResources(cards.peek().getOutputResources());
    }

    /**
     * @return sum of values of all stacked dev cards
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