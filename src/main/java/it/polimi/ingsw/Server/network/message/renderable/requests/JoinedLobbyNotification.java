package it.polimi.ingsw.server.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This class represents a notification sent when the client is added to a lobby.
 */
public class JoinedLobbyNotification extends Notification {
    public final int gameID;

    public JoinedLobbyNotification(int gameID) {
        super("You've been added to a lobby!");
        this.gameID = gameID;
    }

    @Override
    public void render(UI ui) {
        super.render(ui);
        ui.updateGameID(gameID);
    }
}
