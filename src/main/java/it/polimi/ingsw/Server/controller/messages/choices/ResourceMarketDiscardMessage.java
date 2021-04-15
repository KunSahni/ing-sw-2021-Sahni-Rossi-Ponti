package it.polimi.ingsw.server.controller.messages.choices;

import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Map;

/**
 * This class contains a list of Resources from which the Player needs to discard some in order to store them in the depots
 */
public class ResourceMarketDiscardMessage implements Message{
    private final Map<Resource, Integer> resources;

    public ResourceMarketDiscardMessage(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }
}
