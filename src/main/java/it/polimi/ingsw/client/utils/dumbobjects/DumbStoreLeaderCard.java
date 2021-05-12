package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a dumber version of a regular StoreLeaderCard,
 * this class only contains the data stored in a StoreLeaderCard but has none of its logic.
 */
public class DumbStoreLeaderCard extends DumbLeaderCard{
    private final DumbResourceManager storage;
    private final Resource storedResource;

    public DumbStoreLeaderCard(StoreLeaderCard leaderCard) {
        super(leaderCard);
        storage = new DumbResourceManager();
        HashMap<Resource, Integer> storedResources = new HashMap<>();
        storedResources.put(leaderCard.getStoredType(), leaderCard.getResourceCount());
        storage.updateStoredResources(storedResources);
        storedResource = leaderCard.getStoredType();
    }

    /**
     * @param updatedStoredResources an updated map of the stored resources
     */
    public void updateStoredResources(Map<Resource, Integer> updatedStoredResources) {
        this.storage.updateStoredResources(updatedStoredResources);
    }

    public DumbResourceManager getStorage() {
        return storage;
    }

    public Resource getStoredResource() {
        return storedResource;
    }

    @Override
    public StoreLeaderCard convert() {
        return new StoreLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), getStoredResource());
    }
}
