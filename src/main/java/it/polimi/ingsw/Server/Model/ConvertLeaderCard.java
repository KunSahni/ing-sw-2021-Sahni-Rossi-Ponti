package it.polimi.ingsw.server.model;

/**
 * requirement1 requires 2 card of that color
 */
public class ConvertLeaderCard extends LeaderCard {

    private Color requirement1;
    private Color requirement2;
    private Resource convertedResource;

    public ConvertLeaderCard(Color requirement1, Color requirement2, Resource convertedResource) {
        this.requirement1 = requirement1;
        this.requirement2 = requirement2;
        this.convertedResource = convertedResource;
    }
}