package it.polimi.ingsw.network.servertoclient.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This class represents a notification which is sent to the user right before the server closes the ConnectionSocket
 */
public class ConnectionTerminatedNotification extends Notification {
    public ConnectionTerminatedNotification() {
        super("The server terminated your connection after a timeout warning");
    }

    @Override
    public void render(UI ui) {
        ui.renderConnectionTerminatedNotification(message);
    }
}
