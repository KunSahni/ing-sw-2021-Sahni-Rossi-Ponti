package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;

import java.util.List;
import java.util.Map;

public class ProductionCombo {
    private List<Integer> developmentCardSlotsIndexes;
    private Map<Resource, Integer> defaultSlotOutput;
    private Map<DumbLeaderCard, Resource> leaderCardProduction;
    private Map<Resource, Integer> discardedResourcesFromDepots;
    private Map<Resource, Integer> discardedResourcesFromStrongbox;

    public void setDevelopmentCardSlots(List<Integer> developmentCardSlotsIndexes) {
        this.developmentCardSlotsIndexes = developmentCardSlotsIndexes;
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

    public List<Integer> getDevelopmentCardSlotsIndexes() {
        if (developmentCardSlotsIndexes == null)
            return null;
        return List.copyOf(developmentCardSlotsIndexes);
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
