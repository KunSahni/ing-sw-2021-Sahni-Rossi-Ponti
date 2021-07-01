package it.polimi.ingsw.server.model.actiontoken;

/**
 * this enumeration represents all possible types of action token
 */
public enum ActionToken {
    MOVE_BY_TWO("Lorenzo moved by two on faith track"),
    MOVE_AND_SHUFFLE("Action token deck was shuffled and Lorenzo moved by one on faith track"),
    REMOVE_GREEN("Lorenzo removed two green development cards"),
    REMOVE_PURPLE("Lorenzo removed two purple development cards"),
    REMOVE_YELLOW("Lorenzo removed two yellow development cards"),
    REMOVE_BLUE("Lorenzo removed two blue development cards");

    private final String message;

    ActionToken(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}