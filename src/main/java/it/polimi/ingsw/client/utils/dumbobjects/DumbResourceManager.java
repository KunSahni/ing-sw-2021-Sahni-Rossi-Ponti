package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.utils.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a dumber version of a regular ResourceManager,
 * this class only contains data contained in a ResourceManager but has none of its logic.
 */
public class DumbResourceManager implements Serializable {
    private Map<Resource, Integer> storedResources;


    public DumbResourceManager() {
        super();
    }

    /**
     * @param updatedStoredResources an updated map of the stored resources
     */
    public void updateStoredResources(Map<Resource, Integer> updatedStoredResources) {
        this.storedResources = new HashMap<>(updatedStoredResources);
    }

    public int getResourceCount() {
        return storedResources.values()
                .stream()
                .reduce(0, Integer::sum);
    }

    public Map<Resource, Integer> getStoredResources() {
        return storedResources;
    }
}
