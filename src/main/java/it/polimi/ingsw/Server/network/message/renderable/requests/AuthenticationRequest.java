package it.polimi.ingsw.server.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.network.message.renderable.PrivateRenderable;

/**
 * This class represent a request sent by the server.
 * In this request the server asks the client to identify himself with a nickname.
 */
public class AuthenticationRequest extends PrivateRenderable {
    public final String message;

    public AuthenticationRequest() {
        this.message = "Insert your nickname";
    }

    @Override
    public void render(UI ui) {
        ui.renderAuthenticationRequest(message);
    }
}
