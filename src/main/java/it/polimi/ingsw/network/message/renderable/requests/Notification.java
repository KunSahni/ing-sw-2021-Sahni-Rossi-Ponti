package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

/**
 * This class represent a notification which will be displayed on the user's screen
 */
public abstract class Notification extends PrivateRenderable {
    public final String message;

    public Notification(String message) {
        super(null);
        this.message = message;
    }

    @Override
    public void render(UI ui) {
        ui.renderMessage(message);
    }
}
