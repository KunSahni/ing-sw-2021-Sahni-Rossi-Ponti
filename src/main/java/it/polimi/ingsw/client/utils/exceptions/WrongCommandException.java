package it.polimi.ingsw.client.utils.exceptions;

import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * This exception is thrown when user tries to use a command which doesn't exist
 */
public class WrongCommandException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     */
    public WrongCommandException() {
        super(Constants.WRONG_COMMAND);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    protected WrongCommandException(String message) {
        super(message);
    }
}
