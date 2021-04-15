package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.utils.Resource;

/**
 * requirement1 requires 2 card of that color
 */
public class ConvertLeaderCard extends LeaderCard {

    private final Resource convertedResource;

    public ConvertLeaderCard(LeaderCardAbility ability, int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource convertedResource) {
        super(ability, victoryPoints, leaderCardRequirements);
        this.convertedResource = convertedResource;
    }

    public Resource getConvertedResource() {
        return convertedResource;
    }
}