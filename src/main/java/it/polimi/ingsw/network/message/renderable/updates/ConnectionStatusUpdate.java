package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 * This class contains an update regarding a player's connection status which will be saved in the local DumbModel
 */
public class ConnectionStatusUpdate extends BroadcastRenderable {
    private final String nickname;
    private final boolean updatedConnectionStatus;

    public ConnectionStatusUpdate(String nickname, boolean updatedConnectionStatus) {
        this.nickname = nickname;
        this.updatedConnectionStatus = updatedConnectionStatus;
    }

    @Override
    public void render(UI ui) {
        ui.updateConnectionStatus(nickname, updatedConnectionStatus);
    }
}
