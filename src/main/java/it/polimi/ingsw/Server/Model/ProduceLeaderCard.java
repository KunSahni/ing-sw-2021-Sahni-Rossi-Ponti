package it.polimi.ingsw.server.model;


import java.util.*;

public class ProduceLeaderCard extends LeaderCard {

    private final Resource inputResource;
    private final int faithIncrement;

    /**
     * create a productionOutput with the faith increment and the chosenResource
    * @param chosenResource is the resource that the player has chosen
     * @return the productionOutput created by itself
    */

    public ProductionOutput produce(Resource chosenResource) {
        Map<Resource, Integer> map= new HashMap<>();
        map.put(chosenResource, 1);
        return new ProductionOutput(faithIncrement, map);
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