package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains an updated version of a player's strongbox which will be saved in the local DumbModel
 */
public class StrongboxUpdate extends BroadcastRenderable {
    private final String nickname;
    private final Map<Resource, Integer> updatedDepots;

    public StrongboxUpdate(String nickname, Map<Resource, Integer> updatedDepots) {
        this.nickname = nickname;
        this.updatedDepots = new HashMap<>(updatedDepots);
    }

    @Override
    public void render(UI ui) {
        ui.updateStrongbox(nickname, updatedDepots);
    }
}