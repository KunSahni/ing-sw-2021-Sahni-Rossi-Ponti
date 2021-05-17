package it.polimi.ingsw.network.message.renderable;

import it.polimi.ingsw.client.UI;

/**
 * Error message created server-side and transmitted client-side
 * via network
 */
public class ErrorMessage extends PrivateRenderable{
    private final String message;

    public ErrorMessage(String message) {
        super(null);
        this.message = message;
    }

    @Override
    public void render(UI ui) {
        ui.renderErrorMessage(message);
    }
}
