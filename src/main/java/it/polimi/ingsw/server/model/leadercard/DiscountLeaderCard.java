package it.polimi.ingsw.server.model.leadercard;


import it.polimi.ingsw.client.utils.dumbobjects.DumbDiscountLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * Leader Card which reduces the price of Development Cards.
 */
public class DiscountLeaderCard extends LeaderCard {
    /**
     * Resource that the leader card discounts from a development card purchase.
     */
    private final Resource discountedResource;

    public DiscountLeaderCard(int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource discountedResource) {
        super(LeaderCardAbility.DISCOUNT, victoryPoints, leaderCardRequirements);
        this.discountedResource = discountedResource;
    }

    public Resource getDiscountedResource() {
        return discountedResource;
    }

    @Override
    public DumbLeaderCard convertToDumb() {
        return new DumbDiscountLeaderCard(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DiscountLeaderCard that = (DiscountLeaderCard) o;
        return discountedResource == that.discountedResource;
    }
}