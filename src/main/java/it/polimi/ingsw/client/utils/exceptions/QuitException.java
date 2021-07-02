package it.polimi.ingsw.client.utils.exceptions;

/**
 * This exception is thrown when user wants to quit CLI
 */
public class QuitException extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public QuitException() {
    }
}
