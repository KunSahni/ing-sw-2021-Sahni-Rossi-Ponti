package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Map;

/**
 * This class represents the action of discarding some Resources from the ones placed in the depots or in a StoreLeaderCard
 */
public class ResourceDiscardAction implements Forwardable{
    private final Map<Resource, Integer> resources;
    private final PersonalBoard personalBoard;

    /**
     * @param resources the Resources that need to discarded
     * @param personalBoard the PersonalBoard of the Player performing the action
     */
    public ResourceDiscardAction(Map<Resource, Integer> resources, PersonalBoard personalBoard){
        this.resources = resources;
        this.personalBoard = personalBoard;
    }

    @Override
    public void forward() {
        personalBoard.discardFromDepots(resources);
    }
}
