package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

/**
 * This class represent a request sent by the server.
 * In this request the server asks the client to select the number of players for the next game.
 */
public class CreateLobbyRequest extends PrivateRenderable {
    public final String message;

    public CreateLobbyRequest() {
        this.message = "How many players (1-4) will be in the next game?";
    }

    @Override
    public void render(UI ui) {
        ui.renderCreateLobbyRequest(message);
    }
}
