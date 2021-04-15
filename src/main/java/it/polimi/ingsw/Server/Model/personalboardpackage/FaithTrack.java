package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;


public class FaithTrack implements VictoryPointsElement {
    private int position;
    private final List<FavorStatus> popesFavors;
    private final Map<Integer, Integer> victoryPointsSheet;
    private final PersonalBoard personalboard;

    /**
     * @param position       is the starting position of the player
     *                      calculated by game logic depending on
     *                      its turn placement
     * @param personalBoard a reference to board is needed
     *                      to call Game's moveOtherFaithMarkers
     */
    public FaithTrack(int position, PersonalBoard personalBoard) {
        this.position = position;
        this.personalboard = personalBoard;
        popesFavors = new ArrayList<>(Arrays.asList(
                FavorStatus.INACTIVE,
                FavorStatus.INACTIVE,
                FavorStatus.INACTIVE));
        victoryPointsSheet = Map.of( // TODO: parse json
                0, 0,
                3, 1,
                6, 2,
                9, 4,
                12, 6,
                15, 9,
                18, 12,
                21, 16,
                24, 20);
    }

    public int getPosition() {
        return position;
    }

//    public List<FavorStatus> getPopesFavors() {
//        return new ArrayList<>(popesFavors);
//    }

    /**
     * Returns end-game victory points depending on the position
     * on faith track and on the active pope's favors
     */
    @Override
    public int getVictoryPoints() {
        int popesFavorPoints = 0; // Victory points gained from vatican reports
        // Victory points gained from pope's favors
        final int positionPoints = victoryPointsSheet.get(position-(position%3));
        for (FavorStatus singleFavor: popesFavors) {
            if (singleFavor == FavorStatus.ACTIVE) {
                popesFavorPoints+=(2+popesFavors.indexOf(singleFavor));
            }
        }
        return positionPoints + popesFavorPoints;
    }

    /**
     * Moves the marker a number of times equivalent to the parameter
     */
    public void moveMarker(int steps) {
        for (int i = 0; i < steps; i++) {
            moveMarker();
        }
    }
    /**
     * Moves faith marker by one position on the faith track
     * When landing on a pope's place (with inactive status)
     * a vatican report starts
     */
    private void moveMarker() {
        position++;
        if (checkVaticanReport(position)){
            flipPopesFavor(position / 8);
            personalboard.getPlayer().getGame().flipOtherPopesFavor(position / 8);
        }
    }

    /**
     * @param pos indicates a position on the faith track, the method checks
     *            if there is a vatican report that has not yet been invoked
     *            on the given position's tile
     */
    protected boolean checkVaticanReport(int pos) {
        return (pos % 8) == 0 && popesFavors.get(pos / 8) == FavorStatus.INACTIVE;
    }

    /**
     * @param index can be 1,2 or 3. Activates or discards the selected
     *              pope's favor depending on the position of the faith
     *              cross
     */
    public void flipPopesFavor(int index) {
        int vaticanReportSectionSize; // the 3 areas are respectively 4 5 and 6 tiles
        vaticanReportSectionSize = 3 + index;
        if(position > (index * 8) - vaticanReportSectionSize) {
            popesFavors.set(index, FavorStatus.ACTIVE);
        } else {
            popesFavors.set(index, FavorStatus.DISCARDED);
        }
    }
}