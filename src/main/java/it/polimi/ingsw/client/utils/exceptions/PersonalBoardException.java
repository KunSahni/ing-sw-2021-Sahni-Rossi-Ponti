package it.polimi.ingsw.client.utils.exceptions;

import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;

/**
 * This Exception is used as a gimmick to communicate CLI that it should a personal board
 */
public class PersonalBoardException extends Exception {
    String nickname;
    DumbPersonalBoard dumbPersonalBoard;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param dumbPersonalBoard the dumb personal board of the player
     */
    public PersonalBoardException(DumbPersonalBoard dumbPersonalBoard) {
        this.dumbPersonalBoard = dumbPersonalBoard;
    }

    public DumbPersonalBoard getDumbPersonalBoard() {
        return dumbPersonalBoard;
    }
}
