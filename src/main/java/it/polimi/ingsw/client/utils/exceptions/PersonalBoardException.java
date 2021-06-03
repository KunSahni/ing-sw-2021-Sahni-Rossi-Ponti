package it.polimi.ingsw.client.utils.exceptions;

public class PersonalBoardException extends Exception {
    String nickname;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param nickname the nickname of the player
     */
    public PersonalBoardException(String nickname) {
        super(nickname);
    }

    public String getNickname() {
        return nickname;
    }
}
