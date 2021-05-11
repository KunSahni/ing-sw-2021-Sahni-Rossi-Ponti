package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This class represents a notification sent when the client is added to a lobby.
 */
public class JoinedLobbyNotification extends Notification {
    private final int gameID;
    private final int size;

    public JoinedLobbyNotification(int gameID, int size) {
        super("You've been added to a lobby!");
        this.gameID = gameID;
        this.size = size;
    }

    @Override
    public void render(UI ui) {
        super.render(ui);
        ui.createModelView(gameID, size);
    }
}
