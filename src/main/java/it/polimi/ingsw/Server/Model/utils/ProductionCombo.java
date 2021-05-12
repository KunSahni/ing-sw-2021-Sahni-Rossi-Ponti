package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;

import java.util.List;
import java.util.Map;


//todo: this class should be moved to it.polimi.ingsw.network.utils

public class ProductionCombo {
    private List<DumbDevelopmentCard> developmentCards;
    private Map<Resource, Integer> defaultSlotOutput;
    private Map<DumbLeaderCard, Resource> leaderCardProduction;
    private Map<Resource, Integer> discardedResourcesFromDepots;
    private Map<Resource, Integer> discardedResourcesFromStrongbox;

    public void setDevelopmentCardSlots(List<DumbDevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public void setDefaultSlotOutput(Map<Resource, Integer> defaultSlotOutput) {
        this.defaultSlotOutput = defaultSlotOutput;
    }

    public void setLeaderCardProduction(Map<DumbLeaderCard, Resource> leaderCardProduction) {
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
