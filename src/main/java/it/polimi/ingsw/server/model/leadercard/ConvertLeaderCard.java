package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * requirement1 requires 2 card of that color
 */
public class ConvertLeaderCard extends LeaderCard {

    private final Resource convertedResource;

    public ConvertLeaderCard(int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource convertedResource) {
        super(LeaderCardAbility.CONVERT, victoryPoints, leaderCardRequirements);
        this.convertedResource = convertedResource;
    }

    public Resource getConvertedResource() {
        return convertedResource;
    }

    @Override
    public DumbLeaderCard convertToDumb() {
        return new DumbConvertLeaderCard(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConvertLeaderCard that = (ConvertLeaderCard) o;
        return convertedResource == that.convertedResource;
    }
}