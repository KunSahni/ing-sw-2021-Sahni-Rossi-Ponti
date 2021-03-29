package it.polimi.ingsw.server.model;

import java.util.*;


public interface StorageElement {
    void discardResources(Map<Resource, Integer> delResources);

    Map<Resource, Integer> peekResources();
}