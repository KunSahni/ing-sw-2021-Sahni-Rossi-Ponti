package it.polimi.ingsw.network.clienttoserver.action.playeraction;

public class InvalidActionException extends Exception{
    public InvalidActionException(String errorMessage) {
        super(errorMessage);
    }
}
