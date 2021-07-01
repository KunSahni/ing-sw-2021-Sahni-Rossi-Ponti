package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * Leader Card which is able to convert white marbles to its convertedResource type.
 */
public class ConvertLeaderCard extends LeaderCard {
    /**
     * Resource that the leader card converts during a PickFromMarket / SelectMarbles action.
     */
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