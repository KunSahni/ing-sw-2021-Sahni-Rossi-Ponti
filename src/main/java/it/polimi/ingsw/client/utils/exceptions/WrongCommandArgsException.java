package it.polimi.ingsw.client.utils.exceptions;

import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * This exception is used when user types in existing command, but the passed arguments aren't syntactically correct
 */
public class WrongCommandArgsException extends WrongCommandException {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     */
    public WrongCommandArgsException() {
        super(Constants.WRONG_COMMAND_ARGS);
    }
}
