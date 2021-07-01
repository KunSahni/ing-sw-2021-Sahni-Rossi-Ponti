package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;

import java.util.Map;

/**
 * Leader card which can store up to 2 of a specific resource.
 */
public class StoreLeaderCard extends LeaderCard {
    private final ResourceManager storage;
    /**
     * Resource type which can be stored in the storage ResourceManager.
     */
    private final Resource storedResource;

    public StoreLeaderCard(int victoryPoints, LeaderCardRequirements leaderCardRequirements,
                           Resource storedResourceType, Map<Resource, Integer> resources) {
        super(LeaderCardAbility.STORE, victoryPoints, leaderCardRequirements);
        this.storage = new ResourceManager();
        this.storage.storeResources(resources);
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

    public Map<Resource, Integer> getStoredResources() {
        return storage.getStoredResources();
    }

    @Override
    public DumbLeaderCard convertToDumb() {
        return new DumbStoreLeaderCard(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StoreLeaderCard that = (StoreLeaderCard) o;
        return storage.equals(that.storage) && storedResource == that.storedResource;
    }
}