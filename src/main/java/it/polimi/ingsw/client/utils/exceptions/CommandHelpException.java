package it.polimi.ingsw.client.utils.exceptions;

import it.polimi.ingsw.client.utils.constants.Commands;

/**
 * This Exception is thrown when user wants to see help for a specific command
 */
public class CommandHelpException extends Exception {
    private final Commands command;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public CommandHelpException(Commands command) {
        this.command = command;
    }

    public Commands getCommand() {
        return command;
    }
}
