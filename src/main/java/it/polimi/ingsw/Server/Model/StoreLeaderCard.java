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

    public StoreLeaderCard(LeaderCardAbility ability, int victoryPoints, LeaderCardRequirements leaderCardRequirements, ResourceManager storage, Resource storedType) {
        super(ability, victoryPoints, leaderCardRequirements);
        this.storage = storage;
        this.storedType = storedType;
    }
}