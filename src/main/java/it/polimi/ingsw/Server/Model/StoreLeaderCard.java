package it.polimi.ingsw.server.model;

/**
 *
 */
public class StoreLeaderCard extends LeaderCard {
    private ResourceManager storage;
    private Resource storedType;

    public ResourceManager getStorageManager() {
        return storage;
    }

    public Resource getStoredType() {
        return storedType;
    }

    public StoreLeaderCard(ResourceManager storage, Resource storedType) {
        this.storage = storage;
        this.storedType = storedType;
    }
}