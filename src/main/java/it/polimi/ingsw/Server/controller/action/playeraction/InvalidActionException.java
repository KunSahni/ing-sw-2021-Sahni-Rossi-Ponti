package it.polimi.ingsw.server.controller.action.playeraction;

public class InvalidActionException extends Exception{
    public InvalidActionException(String errorMessage) {
        super(errorMessage);
    }
}
