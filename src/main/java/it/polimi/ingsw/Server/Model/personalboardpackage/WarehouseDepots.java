package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.*;


public class WarehouseDepots implements StorageElement {
    private Map<Resource, Integer> storedResources;

    public WarehouseDepots() {
        storedResources = new HashMap<>();
    }

    /**
     * Called by the controller to update the current resource disposition.
     * @param updatedResources contains the new arrangement of resources.
     */
    public void updateResources(Map<Resource, Integer> updatedResources) {
        storedResources = new HashMap<>(updatedResources);
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