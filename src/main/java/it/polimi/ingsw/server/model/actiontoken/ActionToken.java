package it.polimi.ingsw.server.model.actiontoken;

/**
 * this enumeration represents all possible types of action token
 */
public enum ActionToken {
    MOVE_BY_TWO("Lorenzo moved by two"),
    MOVE_AND_SHUFFLE("Action token was shuffled and Lorenzo moved by one"),
    REMOVE_GREEN("Lorenzo removed two green cards"),
    REMOVE_PURPLE("Lorenzo removed two purple cards"),
    REMOVE_YELLOW("Lorenzo removed two yellow cards"),
    REMOVE_BLUE("Lorenzo removed two blue cards");

    private final String message;

    ActionToken(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}