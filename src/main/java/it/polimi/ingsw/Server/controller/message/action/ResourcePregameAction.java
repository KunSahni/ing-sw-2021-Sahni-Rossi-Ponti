package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.Map;

/**
 * This class represents the action of choosing some Resources at the beginning of the game and storing them in the depots
 */
public class ResourcePregameAction implements Forwardable{
    private final Map<Resource, Integer> resources;
    private final PersonalBoard personalBoard;

    /**
     * @param personalBoard the Player's PersonalBoard
     * @param resources the Resources which the Player has picked
     */
    public ResourcePregameAction(PersonalBoard personalBoard, Map<Resource, Integer> resources){
        this.resources = resources;
        this.personalBoard = personalBoard;
    }

    @Override
    public void forward() {
        personalBoard.storeResources(resources);
    }
}
