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



    public static Map<Resource, Integer> getResourceFromMarble(Map<MarketMarble, Integer> request) {
        Map<Resource, Integer> resources = new HashMap<>();
        request.forEach((marble, value) -> resources.put(marble.toResource(), value));
        return resources;
    }
    /**
     * produces and returns a signle resource
     * @param request contains the requested resource
     */

    public static Map<Resource, Integer> getResource(Resource request) {
        Map<Resource, Integer> resource = new HashMap<>();

        resource.put(request, 1);
        return resource;
    }
    /**
     * @return the requested resources
     * @param request is the map that contains the requested resources
     */

    public static Map<Resource, Integer> getResources(Map<Resource, Integer> request) {

        return new HashMap<>(request);
    }

}