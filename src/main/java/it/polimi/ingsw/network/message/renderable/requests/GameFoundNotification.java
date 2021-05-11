package it.polimi.ingsw.network.message.renderable.requests;

/**
 * This class represents a notification which is sent to the user when he tries to reconnect to a game
 * and that exact game is still running, so the user can be reconnected to it.
 */
public class GameFoundNotification extends Notification {
    public GameFoundNotification() {
        super("The requested game was found, you're about to be reconnected!");
    }
}
