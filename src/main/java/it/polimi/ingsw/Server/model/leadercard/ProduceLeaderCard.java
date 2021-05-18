package it.polimi.ingsw.server.model.leadercard;


import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.Resource;

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

    public int getFaithIncrement() {
        return faithIncrement;
    }

    public ProduceLeaderCard(int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource inputResource, int faithIncrement) {
        super(LeaderCardAbility.PRODUCE, victoryPoints, leaderCardRequirements);
        this.inputResource = inputResource;
        this.faithIncrement = faithIncrement;
    }

    @Override
    public DumbLeaderCard convertToDumb() {
        return new DumbProduceLeaderCard(this);
    }

}