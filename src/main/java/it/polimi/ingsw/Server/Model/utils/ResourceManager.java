package it.polimi.ingsw.server.model.utils;

import java.util.*;
import java.util.stream.Collectors;

public class ResourceManager {
    private final Map<Resource, Integer> storedResources;

    public ResourceManager() {
        storedResources = new HashMap<>();
    }

    /**
     * @return shallow copy of currently stored resources
     */
    public Map<Resource, Integer> getStoredResources() {
        return new HashMap<>(storedResources);
    }

    /**
     * @param requirement is the required amount of resources
     * @return true if the requirement is contained, false otherwise
     */
    public boolean contains(Map<Resource, Integer> requirement) {
        return requirement.keySet()
                .stream()
                .map(x -> Optional.ofNullable(storedResources.get(x)).orElse(0) >= requirement.get(x))
                .reduce(true, (a, b) -> a && b);
    }

    /**
     * @param newResources get added to the current resource pool
     */
    public void storeResources(Map<Resource, Integer> newResources) {
        newResources.keySet()
                .forEach(
                        x -> storedResources.put(
                                x, newResources.get(x) + Optional.ofNullable(storedResources.get(x)).orElse(0)
                        )
                );
    }

    /**
     * @param deletedResources get removed from the resource pool
     */
    public void discardResources(Map<Resource, Integer> deletedResources) {
        deletedResources.forEach(
                (k, v) -> storedResources.put(k, storedResources.get(k)-v)
        );
        List<Resource> toRemove = storedResources.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        toRemove.forEach(storedResources::remove);
    }

    /**
     * @return number of resources contained in the pool
     */
    public int getResourceCount() {
        return storedResources.values()
                .stream()
                .reduce(0, Integer::sum);
    }
}
