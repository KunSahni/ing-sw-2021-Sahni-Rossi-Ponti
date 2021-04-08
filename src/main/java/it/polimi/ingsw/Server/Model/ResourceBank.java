package it.polimi.ingsw.server.model;

import java.util.*;
import static it.polimi.ingsw.server.model.MarketMarble.*;
import static it.polimi.ingsw.server.model.Resource.*;

public final class ResourceBank {

    private ResourceBank() {
    }

    /**
     * produces and returns a map of resources
     * @return the requested resources
     * @param request contains the requested resources
     */



    public static Map<Resource, Integer> getResourceFromMarble(Map<MarketMarble, Integer> request) {
        Map<Resource, Integer> resources = new HashMap<>();

        resources.put(COIN, request.get(YELLOW));
        resources.put(SERVANT, request.get(PURPLE));
        resources.put(STONE, request.get(GREY));
        resources.put(SHIELD, request.get(BLUE));

        return resources;
    }
    /**
     * produces and returns a signle resource
     * @param request contains the requested resource
     */

    public static Resource getResources(Resource request) {
        Resource resource;

        resource = request;
        return request;
    }
    /**
     * @return the resources corresponding the passed merkaet marble
     * @param request
     */

    public static Map<Resource, Integer> getResources(Map<Resource, Integer> request) {
        Map<Resource, Integer> resources = new HashMap<>();

        resources.putAll(request);
        return resources;
    }

}