package it.polimi.ingsw.server.network.message.renderable;

/**
 * Error message created server-side and transmitted client-side
 * via network
 */
public class ErrorMessage extends PrivateRenderable{
    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
