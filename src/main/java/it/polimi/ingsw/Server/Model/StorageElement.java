package it.polimi.ingsw.server.model;

import java.util.*;

/**
 * Implemented by all elements that store resources.
 * No method to add resources is present because it
 * is not always implemented, for example depots only
 * take a new arrangement, they do not add on top of
 * what is previously inside.
 */
public interface StorageElement {
    Map<Resource, Integer> getResources();

    void discardResources(Map<Resource, Integer> deletedResources);

    default boolean hasResource(Resource checkedResource){
        int amount = Optional.ofNullable(getResources().get(checkedResource))
                .orElse(0);
        return amount > 0;
    }
}