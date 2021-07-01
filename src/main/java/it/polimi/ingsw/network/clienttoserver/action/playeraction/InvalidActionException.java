package it.polimi.ingsw.network.clienttoserver.action.playeraction;

/**
 * Exception thrown when a PlayerAction meets a condition which makes it illegal
 * to be executed.
 */
public class InvalidActionException extends Exception{
    public InvalidActionException(String errorMessage) {
        super(errorMessage);
    }
}
