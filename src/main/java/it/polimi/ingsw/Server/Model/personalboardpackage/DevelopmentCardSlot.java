package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.*;


public class DevelopmentCardSlot implements DevelopmentSlot, VictoryPointsElement{
    private final Stack<DevelopmentCard> cards;

    public DevelopmentCardSlot() {
        cards = new Stack<>();
    }

    public Stack<DevelopmentCard> viewStack() {
        return (Stack<DevelopmentCard>) cards.clone(); // TODO: understand why he wants the cast
    }
    //TODO: decide if the rest calls peek or viewStack.peek()
    /**
     *
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
    //TODO: add initiator for last round sequence
    public void placeCard(DevelopmentCard card) {
        cards.addElement(card);
    }

    /**
     * @param resources input map of resources that will be consumed by
     *                  the production
     */
    @Override
    public Map<Resource, Integer> produce(Map<Resource, Integer> resources) {
        return ResourceBank.getResource(cards.peek().getOutputResources());
    }
    //TODO: remove canBePlaced from UML

    /**
     * intermediate step created to facilitate personalBoard calculations
     * @return sum of values of all stacked dev cards
     */
    @Override
    public int getVictoryPoints() {
        return cards.stream()
                .mapToInt(DevelopmentCard::getVictoryPoints)
                .sum();
    }
}