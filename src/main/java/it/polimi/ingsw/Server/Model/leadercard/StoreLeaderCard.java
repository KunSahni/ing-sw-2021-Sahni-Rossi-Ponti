package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;

/**
 *
 */
public class StoreLeaderCard extends LeaderCard {
    private final ResourceManager storage;
    private final Resource storedResource;

    public ResourceManager getStorageManager() {
        return storage;
    }

    public Resource getStoredResource() {
        return storedResource;
    }

    public StoreLeaderCard(int victoryPoints, LeaderCardRequirements leaderCardRequirements, ResourceManager storage, Resource storedType) {
        super(LeaderCardAbility.STORE, victoryPoints, leaderCardRequirements);
        this.storage = storage;
        this.storedResource = storedType;
    }
}