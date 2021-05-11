package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This class represents a notification which is sent to the user when he tries to reconnect to a game
 * and that exact game is already finished, so the user can't be reconnected to it.
 */
public class GameNotFoundNotification extends Notification{

    public GameNotFoundNotification() {
        super("We're sorry, but the requested game is finished, you can either join a new game or quit the app.");
    }

    @Override
    public void render(UI ui) {
        super.render(ui);
        ui.createModelView(-1, 0);
    }
}
