package it.polimi.ingsw.network.message.renderable.requests;

/**
 * This class represents a notification which is sent when the lobby is completed
 * and therefore the actual game can be started.
 */
public class GameStartedNotification extends Notification{
    public GameStartedNotification() {
        super("The game is about to start!");
    }
}
