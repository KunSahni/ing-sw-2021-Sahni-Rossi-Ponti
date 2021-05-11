package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * This is a dumber version of a regular ConvertLeaderCard,
 * this class only contains the data stored in a ConvertLeaderCard but has none of its logic.
 */
public class DumbConvertLeaderCard extends DumbLeaderCard{
    private final Resource convertedResource;

    public DumbConvertLeaderCard(ConvertLeaderCard leaderCard) {
        super(leaderCard);
        convertedResource = leaderCard.getConvertedResource();
    }

    public Resource getConvertedResource() {
        return convertedResource;
    }
}
