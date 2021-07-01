package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * An element which groups all information needed to start a production.
 */
public class ProductionCombo implements Serializable {
    /**
     * Development cards which will take part in the production.
     */
    private List<DumbDevelopmentCard> developmentCards;
    /**
     * A map which represents the resource the player has chosen as output to
     * a default slot production activation.
     */
    private Map<Resource, Integer> defaultSlotOutput;
    /**
     * Map which links each activated produce leader card with the resource the
     * player has selected as output.
     */
    private Map<DumbProduceLeaderCard, Resource> leaderCardProduction;
    /**
     * Resources that will get discarded from store leader cards and warehouse depots
     */
    private Map<Resource, Integer> discardedResourcesFromDepots;
    /**
     * Resources that will get discarded from the strongbox.
     */
    private Map<Resource, Integer> discardedResourcesFromStrongbox;

    public void setDevelopmentCards(List<DumbDevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public void setDefaultSlotOutput(Map<Resource, Integer> defaultSlotOutput) {
        this.defaultSlotOutput = defaultSlotOutput;
    }

    public void setLeaderCardProduction(Map<DumbProduceLeaderCard, Resource> leaderCardProduction) {
        this.leaderCardProduction = leaderCardProduction;
    }

    public void setDiscardedResourcesFromDepots(Map<Resource, Integer> discardedResourcesFromDepots) {
        this.discardedResourcesFromDepots = discardedResourcesFromDepots;
    }

    public void setDiscardedResourcesFromStrongbox(Map<Resource, Integer> discardedResourcesFromStrongbox) {
        this.discardedResourcesFromStrongbox = discardedResourcesFromStrongbox;
    }

    public List<DumbDevelopmentCard> getDevelopmentCards() {
        if (developmentCards == null)
            return null;
        return List.copyOf(developmentCards);
    }

    public Map<Resource, Integer> getDefaultSlotOutput() {
        if(defaultSlotOutput == null)
            return null;
        return Map.copyOf(defaultSlotOutput);
    }

    public Map<DumbLeaderCard, Resource> getLeaderCardProduction() {
        if(leaderCardProduction == null)
            return null;
        return Map.copyOf(leaderCardProduction);
    }

    public Map<Resource, Integer> getDiscardedResourcesFromDepots() {
        if(discardedResourcesFromDepots == null)
            return null;
        return Map.copyOf(discardedResourcesFromDepots);
    }

    public Map<Resource, Integer> getDiscardedResourcesFromStrongbox() {
        if(discardedResourcesFromStrongbox == null)
            return null;
        return Map.copyOf(discardedResourcesFromStrongbox);
    }
}
