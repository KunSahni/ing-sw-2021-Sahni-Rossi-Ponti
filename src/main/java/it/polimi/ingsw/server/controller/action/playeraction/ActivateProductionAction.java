package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.personalboard.DefaultSlot;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class ActivateProductionAction extends PlayerAction {
    private final ProductionCombo productionCombo;

    public ActivateProductionAction(ProductionCombo productionCombo) {
        this.productionCombo = productionCombo;
    }

    @Override
    public GameAction execute() {
        List<ProductionOutput> productionOutputs = new ArrayList<>();
        Optional.ofNullable(productionCombo.getDefaultSlotOutput()).ifPresent(
                defaultSlotOutput -> productionOutputs.add(DefaultSlot.produce(defaultSlotOutput))
        );
        Optional.ofNullable(productionCombo.getLeaderCardProduction()).ifPresent(
                leaderCardResourceMap -> leaderCardResourceMap.forEach(
                        (dumbCard, outputResource) -> productionOutputs.add(
                                ((ProduceLeaderCard) dumbCard.convert()).produce(outputResource))
                )
        );
        Optional.ofNullable(productionCombo.getDevelopmentCards()).ifPresent(
                cards -> cards.stream().map(DumbDevelopmentCard::convert)
                        .forEach(card -> productionOutputs.add(card.produce()))
        );
        // Store all outputs in the strongbox
        productionOutputs.forEach(productionOutput ->
                player.getPersonalBoard().storeInStrongbox(productionOutput.getResources()));
        // Move the marker on the Faith Track
        productionOutputs.forEach(productionOutput ->
                super.moveFaithMarker(productionOutput.getFaithIncrement()));
        // Discard resources
        player.getPersonalBoard()
                .discardFromDepots(productionCombo.getDiscardedResourcesFromDepots());
        player.getPersonalBoard()
                .discardFromStrongbox(productionCombo.getDiscardedResourcesFromStrongbox());
        player.addAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (!player.isValidNextAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION))
            throw new InvalidActionException("You cannot activate a production at this time.");
        if (productionComboIsEmpty())
            throw new InvalidActionException("You supplied an empty Production Combo");
        if (productionCombo.getDefaultSlotOutput().values().stream().reduce(0, Integer::sum) != 1)
            throw new InvalidActionException("You cannot produce more than one resource with your" +
                    " default slot activation");
        if (productionCombo.getDevelopmentCards() != null) {
            if (!developmentCardsAvailable())
                throw new InvalidActionException("The development cards you have supplied do not " +
                        "match the ones available in your Development Slots.");
        }
        if (productionCombo.getLeaderCardProduction() != null) {
            if (!leaderCardsMatch())
                throw new InvalidActionException("You have supplied Leader Cards that are not " +
                        "available on your personal board.");
            if (!leaderCardsAreProduce())
                throw new InvalidActionException("You must supply Produce Type Leader Cards to " +
                        "activate a production.");
        }
        if (!discardMapsMatchProductionCosts())
            throw new InvalidActionException("The resources that you have selected to discard do " +
                    "not match production costs.");
        if (!player.getPersonalBoard()
                .depotsContainResources(productionCombo.getDiscardedResourcesFromDepots())
                || !player.getPersonalBoard()
                .strongboxContainsResources(productionCombo.getDiscardedResourcesFromStrongbox()))
            throw new InvalidActionException("You are trying to discard from your storages " +
                    "resources you do not have.");
    }

    private boolean productionComboIsEmpty() {
        return productionCombo.getDevelopmentCards() == null
                && productionCombo.getLeaderCardProduction() == null
                && productionCombo.getDefaultSlotOutput() == null;
    }

    private boolean developmentCardsAvailable() {
        return player.getPersonalBoard().getDevelopmentCardSlots().stream()
                .map(DevelopmentCardSlot::peek).collect(Collectors.toList())
                .containsAll(productionCombo.getDevelopmentCards().stream()
                        .map(DumbDevelopmentCard::convert).collect(Collectors.toList()));
    }

    private boolean leaderCardsMatch() {
        return productionCombo.getLeaderCardProduction().keySet().stream()
                .map(DumbLeaderCard::convert)
                .allMatch(card -> player.getPersonalBoard().getLeaderCards().stream()
                        .filter(LeaderCard::isActive)
                        .collect(Collectors.toList())
                        .contains(card));
    }

    private boolean leaderCardsAreProduce() {
        return productionCombo.getLeaderCardProduction().keySet().stream()
                .map(DumbLeaderCard::convert)
                .allMatch(card -> card instanceof ProduceLeaderCard);
    }

    private boolean discardMapsMatchProductionCosts() {
        // Merge depots discards and strongbox discards
        Map<Resource, Integer> totalDiscardMap =
                new HashMap<>(productionCombo.getDiscardedResourcesFromDepots());
        productionCombo.getDiscardedResourcesFromStrongbox().forEach(
                (key, value) -> totalDiscardMap.compute(key, (k, v) -> (v == null)
                        ? value : v + value)
        );
        // Subtract the dev card inputs from the total discards map
        productionCombo.getDevelopmentCards()
                .forEach(card -> card.getInputResources().forEach(
                        (key, value) -> totalDiscardMap.compute(key, (k, v) -> (v == null)
                                ? -value : v - value)
                ));
        // Subtract leader card inputs from the total discards map
        productionCombo.getLeaderCardProduction().keySet()
                .stream()
                .map(DumbLeaderCard::convert)
                .forEach(card -> totalDiscardMap.compute(((ProduceLeaderCard) card).getInputResource(),
                        (k, v) -> (v == null) ? -1 : v - 1));
        // If any field of the discard map went negative while counting dev cards
        // costs it means that the player did not supply enough resources to cover
        // the production
        if (totalDiscardMap.values().stream().allMatch(value -> value >= 0)) {
            // Count how many resources are left in the discard map
            int resourcesLeft = totalDiscardMap.values().stream().reduce(0, Integer::sum);
            int resourcesNeeded = 0;
            // If the default slot gets activated 2 resources are needed
            if (productionCombo.getDefaultSlotOutput() != null)
                resourcesNeeded += 2;
            return resourcesLeft == resourcesNeeded;
        } else {
            return false;
        }
    }
}
