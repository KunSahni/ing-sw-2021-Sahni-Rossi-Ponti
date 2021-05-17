package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains an updated version of a player's strongbox which will be saved in the local DumbModel
 */
public class StrongboxUpdate extends BroadcastRenderable {
    private final String nickname;
    private final Map<Resource, Integer> updatedStrongbox;

    public StrongboxUpdate(String nickname, ResourceManager updatedStrongbox) {
        this.nickname = nickname;
        this.updatedStrongbox = new HashMap<>(updatedStrongbox.getStoredResources());
    }

    @Override
    public void render(UI ui) {
        ui.updateStrongbox(nickname, updatedStrongbox);
    }
}