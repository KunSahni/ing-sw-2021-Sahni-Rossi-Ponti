package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.network.clienttoserver.action.gameaction.GameAction;
import it.polimi.ingsw.network.servertoclient.renderable.ConfirmationMessageType;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents the action of purchasing a DevelopmentCard
 */
public class BuyDevelopmentCardAction extends PlayerAction {
    private final Level level;
    private final Color color;
    private final int index;
    private final Map<Resource, Integer> discardedResourcesFromDepots;
    private final Map<Resource, Integer> discardedResourcesFromStrongbox;

    /**
     * @param level                           the level of the chosen LeaderCard
     * @param color                           the color of the chosen LeaderCard
     * @param index                           index of the DevelopmentCardSlot where the card
     *                                        will be place
     * @param discardedResourcesFromDepots    the resources discarded from the depots in order to
     *                                        buy the DevelopmentCard
     * @param discardedResourcesFromStrongbox the resources discarded from the strongbox in order
     *                                        to buy the DevelopmentCard
     */
    public BuyDevelopmentCardAction(Level level,
                                    Color color,
                                    int index,
                                    Map<Resource, Integer> discardedResourcesFromDepots,
                                    Map<Resource, Integer> discardedResourcesFromStrongbox) {
        this.level = level;
        this.color = color;
        this.index = index;
        this.discardedResourcesFromDepots = Objects.isNull(discardedResourcesFromDepots)
                ? new HashMap<>() : discardedResourcesFromDepots;
        this.discardedResourcesFromStrongbox = Objects.isNull(discardedResourcesFromStrongbox)
                ? new HashMap<>() : discardedResourcesFromStrongbox;
    }

    @Override
    public GameAction execute() {
        player.getPersonalBoard().placeDevelopmentCard(
                game.getDevelopmentCardsBoard().pick(level, color),
                index
        );
        player.getPersonalBoard().discardFromDepots(discardedResourcesFromDepots);
        player.getPersonalBoard().discardFromStrongbox(discardedResourcesFromStrongbox);
        player.addAction(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (!player.isValidNextAction(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION))
            throw new InvalidActionException("You cannot buy a Development Card at this time");
        if (game.getDevelopmentCardsBoard().peekCard(level, color) == null)
            throw new InvalidActionException(color + " " + level + "Development Cards are not " +
                    "available on the board anymore.");
        if (!passedResourcesMatchCardCost())
            throw new InvalidActionException("You did not pass the correct amount of resources to" +
                    " purchase the selected Development Card");
        if (!player.getPersonalBoard().depotsContainResources(discardedResourcesFromDepots)
                || !player.getPersonalBoard()
                .strongboxContainsResources(discardedResourcesFromStrongbox))
            throw new InvalidActionException("Your storages do not have enough resources.");
        if (!player.getPersonalBoard().getDevelopmentCardSlots().get(index - 1)
                .canPlaceCard(game.getDevelopmentCardsBoard().peekCard(level, color)))
            throw new InvalidActionException("You cannot place a " + level + " Development Card " +
                    " in the Development Cards Slot number " + index);
    }

    /**
     * Checks if the resources that will get discarded match the effective development card
     * cost.
     */
    private boolean passedResourcesMatchCardCost() {
        Map<Resource, Integer> cost =
                game.getDevelopmentCardsBoard().peekCard(level, color).getCost();
        // Apply discounts if present
        player.getPersonalBoard()
                .getLeaderCards()
                .stream()
                .filter(card -> card.isActive() &&
                        card.getAbility().equals(LeaderCardAbility.DISCOUNT))
                .map(DiscountLeaderCard.class::cast)
                .forEach(card -> cost.computeIfPresent(
                        card.getDiscountedResource(),
                        (k, v) -> v - 1
                ));
        // Worst case scenario: two DiscountLeaderCards are active and
        // reduce two of the cost Map values to 0. To reformat the map
        // at most two 0-value entries are removed.
        cost.values().remove(0);
        cost.values().remove(0);
        // Merge the two discarded resources maps
        Map<Resource, Integer> passedResources = new HashMap<>(discardedResourcesFromDepots);
        discardedResourcesFromStrongbox.forEach((key, value) ->
                passedResources.merge(key, value, Integer::sum));
        return cost.equals(passedResources);
    }

    @Override
    public ConfirmationMessageType getConfirmationMessage() {
        return ConfirmationMessageType.BUY_DEVELOPMENT_CARD;
    }
}