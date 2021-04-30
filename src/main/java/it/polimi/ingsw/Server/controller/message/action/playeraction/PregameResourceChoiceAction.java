package it.polimi.ingsw.server.controller.message.action.playeraction;

import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Map;

/**
 * This class represents the action of choosing some Resources at the beginning of the game and storing them in the depots
 */
public class PregameResourceChoiceAction extends PlayerAction {
    private final Map<Resource, Integer> resources;

    /**
     * @param resources the Resources which the Player has picked
     */
    public PregameResourceChoiceAction(Map<Resource, Integer> resources){
        this.resources = resources;
    }

    @Override
    public void execute() {
        personalBoard.storeInDepots(resources);
    }

    @Override
    public boolean isAllowed() {
        return
    }
}
