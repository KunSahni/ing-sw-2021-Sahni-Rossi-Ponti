package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This class represents a notification sent to the client when he joins an incomplete lobby.
 */
public class WaitingForPlayersNotification extends Notification{
    public WaitingForPlayersNotification() {
        super("Waiting for players...");
    }

    @Override
    public void render(UI ui) {
        ui.renderWaitingForPlayersNotification(message);
    }
}
