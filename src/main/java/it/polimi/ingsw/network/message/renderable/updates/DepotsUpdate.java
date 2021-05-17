package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains an updated version of a player's depots which will be saved in the local DumbModel
 */
public class DepotsUpdate extends BroadcastRenderable {
    private final String nickname;
    private final Map< Resource, Integer> updatedDepots;

    public DepotsUpdate(String nickname, ResourceManager updatedDepots) {
        this.nickname = nickname;
        this.updatedDepots = new HashMap<>(updatedDepots.getStoredResources());
    }

    @Override
    public void render(UI ui) {
        ui.updateDepots(nickname, updatedDepots);
    }
}
