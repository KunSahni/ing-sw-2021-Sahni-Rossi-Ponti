package it.polimi.ingsw.server.model;


import java.util.*;

public class ProduceLeaderCard extends LeaderCard {

    private Resource inputResource;
    private int faithIncrement;

    /**
     * create a productionOutput with the faith increment and the chosenResource
    * @param chosenResource
     * @return the productionOutput created by itself
    */

    public ProductionOutput produce(Resource chosenResource) {
        Map<Resource, Integer> map= new HashMap<>();
        map.put(chosenResource, 1);
        ProductionOutput productionOutput = new ProductionOutput(faithIncrement, map);
        return productionOutput;
    }

    public Resource getInputResource() {
        return inputResource;
    }

    public ProduceLeaderCard(LeaderCardAbility ability, int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource inputResource, int faithIncrement) {
        super(ability, victoryPoints, leaderCardRequirements);
        this.inputResource = inputResource;
        this.faithIncrement = faithIncrement;
    }
}