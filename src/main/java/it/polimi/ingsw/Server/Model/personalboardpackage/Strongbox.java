package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.model.*;
import java.util.*;


public class Strongbox implements StorageElement {
    private final Map<Resource, Integer> storedResources;

    public Strongbox() {
        storedResources = new HashMap<>();
    }

    /**
     * Adds the new resources to the storage
     * @param addedResources contains the new resources
     */
    public void storeResources(Map<Resource, Integer> addedResources) {
        addedResources.forEach((k, v) -> storedResources.put(k, storedResources.get(k) + v));
    }

    /**
     * Removes the given amounts of resources
     * @param deletedResources map indicates which and how many resources should be
     *                         deleted
     */
    @Override
    public void discardResources(Map<Resource, Integer> deletedResources) {
        deletedResources.forEach((k, v) -> storedResources.put(k, storedResources.get(k)-v));
    }

    /**
     * Creates a shallow copy of the current resources in the depots and returns it
     * to the caller
     * @return shallow copy
     */
    @Override
    public Map<Resource, Integer> peekResources() {
        return new HashMap<>(storedResources);
    }
}