package it.polimi.ingsw.server.model.personalboard;


/**
 * Class representing a Faith Track used in a single-player match, extends the regular
 * Faith Track Class by handling the Black Cross' behavior.
 */
public class SinglePlayerFaithTrack extends FaithTrack {
    private int blackCrossPosition;

    private SinglePlayerFaithTrack() {
        super();
        blackCrossPosition = 0;
    }

    /**
     * Returns the position of the Black Cross on the Faith Track
     * @return integer representing the position
     */
    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * Moves the Black Cross up by one position on the faith track.
     * When landing on a pope's place (with inactive status) a
     * vatican report starts.
     */
    public void moveBlackCross() {
        blackCrossPosition++;
        changesHandler.writeFaithTrack(nickname, this);
    }
}