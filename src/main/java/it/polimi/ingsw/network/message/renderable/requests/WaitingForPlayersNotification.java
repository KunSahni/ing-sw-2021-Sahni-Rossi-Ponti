package it.polimi.ingsw.network.message.renderable.requests;

/**
 * This class represents a notification sent to the client when he joins an incomplete lobby.
 */
public class WaitingForPlayersNotification extends Notification{
    public WaitingForPlayersNotification() {
        super("Waiting for players...");
    }
}
