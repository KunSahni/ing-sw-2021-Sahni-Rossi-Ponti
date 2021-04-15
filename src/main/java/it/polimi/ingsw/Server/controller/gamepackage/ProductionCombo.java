package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentCardSlot;

import java.util.List;
import java.util.Map;

public class ProductionCombo {
    private List<DevelopmentCardSlot> developmentCardSlots;
    private Map<Resource, Integer> defaultSlotOutput;
    private List<LeaderCard> leaderCards;
    private Map<LeaderCard, Resource> leaderCardOutputs;
    private Map<String, Map<Resource, Integer>> discardedResources;

    public ProductionCombo() {
        super();
    }

    public void setDevelopmentCardSlots(List<DevelopmentCardSlot> developmentCardSlots) {
        this.developmentCardSlots = developmentCardSlots;
    }

    public void setDefaultSlotOutput(Map<Resource, Integer> defaultSlotOutput) {
        this.defaultSlotOutput = defaultSlotOutput;
    }

    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public void setLeaderCardOutputs(Map<LeaderCard, Resource> leaderCardOutputs) {
        this.leaderCardOutputs = leaderCardOutputs;
    }

    public void setDiscardedResources(Map<String, Map<Resource, Integer>> discardedResources) {
        this.discardedResources = discardedResources;
    }

    public List<DevelopmentCardSlot> getDevelopmentCardSlots() {
        return developmentCardSlots;
    }

    public Map<Resource, Integer> getDefaultSlotOutput() {
        return defaultSlotOutput;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public Map<LeaderCard, Resource> getLeaderCardOutputs() {
        return leaderCardOutputs;
    }

    public Map<String, Map<Resource, Integer>> getDiscardedResources() {
        return discardedResources;
    }

}
