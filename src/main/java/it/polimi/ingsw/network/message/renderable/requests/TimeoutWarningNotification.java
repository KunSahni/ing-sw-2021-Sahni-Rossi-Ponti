package it.polimi.ingsw.network.message.renderable.requests;

/**
 * This class represents a notification sent when the client hasn't performed any action in the first 60 seconds of his turn.
 */
public class TimeoutWarningNotification extends Notification{
    public final int remainingTime;

    public TimeoutWarningNotification(int remainingTime) {
        super("No activity detected in the last 60 seconds, you will be disconnected if no other action will be performed in the next 60 seconds");
        this.remainingTime = remainingTime;
    }
}
