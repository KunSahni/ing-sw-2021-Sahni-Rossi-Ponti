package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * This is a dumber version of a regular DiscountLeaderCard,
 * this class only contains the data stored in a DiscountLeaderCard but has none of its logic.
 */
public class DumbDiscountLeaderCard extends DumbLeaderCard{
    private final Resource discountedResource;

    public DumbDiscountLeaderCard(DiscountLeaderCard leaderCard) {
        super(leaderCard);
        this.discountedResource = leaderCard.getDiscountedResource();
    }

    public Resource getDiscountedResource() {
        return discountedResource;
    }


    @Override
    public DiscountLeaderCard convert() {
        return new DiscountLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), discountedResource);
    }
}
