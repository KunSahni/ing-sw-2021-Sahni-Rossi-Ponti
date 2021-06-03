package it.polimi.ingsw.client.utils.exceptions;

import it.polimi.ingsw.client.utils.constants.Constants;

public class InvalidArgsException extends Throwable {

    /**
     * Constructs a new throwable with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * <p>The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created throwable.
     *
     */
    public InvalidArgsException() {
        super(Constants.INVALID_ARGS);
    }
}
