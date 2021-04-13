package it.polimi.ingsw.server.model;


public class DiscountLeaderCard extends LeaderCard {

    private final Resource discountedResource;

    public DiscountLeaderCard(LeaderCardAbility ability, int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource discountedResource) {
        super(ability, victoryPoints, leaderCardRequirements);
        this.discountedResource = discountedResource;
    }

    public Resource getDiscountedResource() {
        return discountedResource;
    }
}