package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * This is a dumber version of a regular ProduceLeaderCard,
 * this class only contains the data stored in a ProduceLeaderCard but has none of its logic.
 */
public class DumbProduceLeaderCard extends DumbLeaderCard{
    private final Resource inputResource;
    private final int faithIncrement;

    public DumbProduceLeaderCard(ProduceLeaderCard leaderCard) {
        super(leaderCard);
        inputResource = leaderCard.getInputResource();
        faithIncrement = leaderCard.getFaithIncrement();
    }

    public Resource getInputResource() {
        return inputResource;
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }


    @Override
    public ProduceLeaderCard convert() {
        return new ProduceLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), inputResource, faithIncrement);
    }
}
