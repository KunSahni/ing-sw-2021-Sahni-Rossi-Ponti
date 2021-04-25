package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;

import java.util.Map;

public class StoreLeaderCard extends LeaderCard {
    private final ResourceManager storage;
    private final Resource storedResource;

    public StoreLeaderCard(int victoryPoints, LeaderCardRequirements leaderCardRequirements, Resource storedResourceType) {
        super(LeaderCardAbility.STORE, victoryPoints, leaderCardRequirements);
        this.storage = new ResourceManager();
        this.storedResource = storedResourceType;
    }

    /**
     * Returns the number of resources contained in the StoreLeaderCard
     */
    public int getResourceCount() {
        return storage.getResourceCount();
    }

    /**
     * Discards the given map from the currently stored resources. This method expects
     * a properly formed map, all resources in the parameter must be present it the
     * StoreLeaderCard, undefined behavior may occur otherwise.
     */
    public void discardResources(Map<Resource, Integer> deletedResources) {
        storage.discardResources(deletedResources);
    }

    /**
     * Adds the given map to the StoreLeaderCard storage. A properly formed map must be
     * passed, the StoreLeaderCard must have enough storing space to contain the new
     * resources, undefined behavior may occur otherwise.
     */
    public void storeResources(Map<Resource, Integer> newResources) {
        storage.storeResources(newResources);
    }

    /**
     * Returns the StoreLeaderCard's stored resource type.
     */
    public Resource getStoredType() {
        return storedResource;
    }
}