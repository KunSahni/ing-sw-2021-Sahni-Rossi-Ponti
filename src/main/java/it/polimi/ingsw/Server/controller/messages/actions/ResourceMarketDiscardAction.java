package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.controller.gamepackage.Player;
import it.polimi.ingsw.server.model.Resource;

import java.util.Map;

/**
 * This class represents the action of discarding some Resources from the ones just picked from the Market
 */
public class ResourceMarketDiscardAction implements Forwardable{
    private final Map<Resource, Integer> resources;
    private final Player player;

    /**
     * @param resources the Resources that need to discarded
     * @param player the Player performing the action
     */
    public ResourceMarketDiscardAction(Map<Resource, Integer> resources, Player player){
        this.resources = resources;
        this.player = player;
    }

    @Override
    public void forward() {
        player.discardResources(resources);
    }
}
