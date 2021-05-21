package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.*;
import static it.polimi.ingsw.server.model.market.MarketMarble.*;
import static it.polimi.ingsw.server.model.utils.Resource.*;

public final class ResourceBank {

    private ResourceBank() {
    }

    /**
     * produces and returns a map of resources
     * @return the resources corresponding the passed market marbles
     * @param request contains the requested resources
     */
    public static Map<Resource, Integer> getResourcesFromMarbles(Map<MarketMarble, Integer> request) {
        Map<Resource, Integer> resources = new HashMap<>();
        request.forEach((marble, value) -> resources.put(marble.toResource(), value));
        return resources;
    }

    /**
     * @return the requested resources
     * @param request is the map that contains the requested resources
     */
    public static Map<Resource, Integer> getResources(Map<Resource, Integer> request) {

        return new HashMap<>(request);
    }

}