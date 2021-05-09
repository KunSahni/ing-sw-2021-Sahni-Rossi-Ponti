package it.polimi.ingsw.network.message.renderable.requests;

import it.polimi.ingsw.client.UI;

/**
 * This class represents a notification which is sent to the user when he tries to reconnect to a game
 * and that exact game is still running, but there's no reference to a player with the given nickname.
 */
public class WrongNicknameNotification extends Notification{

    protected WrongNicknameNotification() {
        super("The requested game was found, but no player with your nickname was found in it.");
    }

    @Override
    public void render(UI ui) {
        super.render(ui);
    }
}