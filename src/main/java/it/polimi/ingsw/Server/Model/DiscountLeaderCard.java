package it.polimi.ingsw.server.model;


public class DiscountLeaderCard extends LeaderCard {

    private Color requirement1;

    private Color requirement2;

    private Resource discountedResource;

    public DiscountLeaderCard(Color requirement1, Color requirement2, Resource discountedResource) {
        this.requirement1 = requirement1;
        this.requirement2 = requirement2;
        this.discountedResource = discountedResource;
    }
}