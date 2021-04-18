package it.polimi.ingsw.server.model.personalboardpackage;


public class SinglePlayerFaithTrack extends FaithTrack {
    private int blackCrossPosition;

    /**
     * @param position      is the starting position of the player
     *                      calculated by game logic depending on
     *                      its turn placement
     * @param personalBoard a reference to board is needed
     *                      to call Game's moveOtherFaithMarkers
     */
    public SinglePlayerFaithTrack(int position, PersonalBoard personalBoard) {
        super(position, personalBoard);
        blackCrossPosition = 0;
    }

    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * Move Lorenzo's cross by one tile. In case a pope's place is
     * reached, start a vatican report that will affect the only
     * player
     */
    public void moveBlackCross() {
        blackCrossPosition++;
        if (checkVaticanReport(blackCrossPosition)){
            flipPopesFavor(blackCrossPosition / 8);
        }
    }
}