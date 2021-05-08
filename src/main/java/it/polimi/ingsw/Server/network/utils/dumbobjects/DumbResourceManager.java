package it.polimi.ingsw.server.network.utils.dumbobjects;

import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Map;

/**
 * This is a dumber version of a regular ResourceManager,
 * this class only contains data contained in a ResourceManager but has none of its logic.
 */
public class DumbResourceManager {
    private Map<Resource, Integer> storedResources;

    public DumbResourceManager(Map<Resource, Integer> storedResources) {
        this.storedResources = storedResources;
    }

    public Map<Resource, Integer> getStoredResources() {
        return storedResources;
    }
}
