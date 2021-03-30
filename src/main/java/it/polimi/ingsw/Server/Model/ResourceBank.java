package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;


import Resource;
import jdk.internal.loader.Resource;

public class ResourceBank {
    /**
     * produces and returns a map of resources
     * @return the requested resources
    * @param request contains the requested resources
    */


    public Map<Resource, Integer> getResource(Map<Resource, Integer> request) {
        Map<Resource, Integer> resources = new HashMap<>();

        resources.put(COIN, request.get(COIN));
        resources.put(SERVANT, request.get(SERVANT));
        resources.put(STONE, request.get(STONE));
        resources.put(SHIELD, request.get(SHIELD));

        return resources;
    }
    /**
     * produces and returns a signle resource
    * @param request contains the requested resource
    */

    public Resource getResource(Resource request) {
        Resource resource;

        resource = request;
        return request;
    }
    /**
     * @return the resources corresponding the passed merkaet marble
    * @param request
    */

    public Map<Resource, Integer> getResource(Map<MarketMarble, Integer> request) {
        Map<MarketMarble, Integer> resources = new HashMap<>();

        resources.put(COIN, request.get(YELLOW));
        resources.put(SHIELD, request.get(BLUE));
        resources.put(STONE, request.get(GREY));
        resources.put(SERVANT, request.get(PURPLE));

        return resources;
    }

}