package it.polimi.ingsw.client.utils.dumbobjects;

/**
 * This is a dumber version of a regular SinglePlayerFaithTrack,
 * this class only contains data contained in a SinglePlayerFaithTrack but has none of its logic.
 */
public class DumbSinglePlayerFaithTrack extends DumbFaithTrack{
    private int blackCrossPosition;

    public void updateBlackCrossPosition(int blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
    }

    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }
}
