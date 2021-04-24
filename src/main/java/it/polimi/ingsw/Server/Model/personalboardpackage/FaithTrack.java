package it.polimi.ingsw.server.model.personalboardpackage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
//import java.util.concurrent.Flow.Publisher;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;

/**
 * Class representing a standard Faith Track used in a multi-player match.
 */
public class FaithTrack implements VictoryPointsElement /*, Publisher*/ {
    private int faithMarkerPosition;
    private final List<FavorStatus> popesFavors;
    private final Player player;

    public FaithTrack(Player player) {
        // Player marked by the Inkwell (position 1) and the one right
        // after start from 0 on the faith track. Players in position 3
        // and 4 have a 1 tile advantage.
        this.faithMarkerPosition = player.getPosition() / 3;
        // Player reference is saved to retrieve current game when
        // a Vatican Report starts and all boards need to have their
        // Faith Tracks updated.
        this.player = player;
        this.popesFavors = new ArrayList<>(Arrays.asList(FavorStatus.INACTIVE, FavorStatus.INACTIVE, FavorStatus.INACTIVE));
    }

    /**
     * Returns the position of the Faith Marker on the Faith Track.
     * Ranges from 0 to 24.
     * @return position integer.
     */
    public int getFaithMarkerPosition() {
        return faithMarkerPosition;
    }

    /**
     * Returns a List of FavorStatus. The elements are sorted
     * in the same fashion as they are displayed on the board,
     * first in the list being the least valuable.
     * @return List of FavorStatus enum elements.
     */
    public List<FavorStatus> getPopesFavors() {
        return new ArrayList<>(popesFavors);
    }

    /**
     * Returns end-game victory points depending on the position
     * on faith track and on active pope's favors.
     * @return number of victory points obtained from the board.
     */
    @Override
    public int getVictoryPoints() {
        return calculatePopesFavorsVictoryPoints() + calculateTrackPositionVictoryPoints();
    }

    /**
     * Calculate the amount of Victory Points earned from activating
     * Pope's Favors.
     * @return number of points.
     */
    private int calculatePopesFavorsVictoryPoints() {
        int popesFavorPoints = 0;
        for (int i = 0; i < popesFavors.size(); i++) {
            if (popesFavors.get(i) == FavorStatus.ACTIVE) {
                popesFavorPoints += i + 2;
            }
        }
        return popesFavorPoints;
    }

    /**
     * Calculates the amount of Victory Points earned from moving
     * forward on the Faith Track.
     * @return number of points.
     */
    private int calculateTrackPositionVictoryPoints() {
        try {
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/FaithTrackPoints.json"));
            Map<String, Double> victoryPointsMap = new Gson().fromJson(reader, Map.class);
            return victoryPointsMap.get(Integer.toString(getFaithMarkerPosition())).intValue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Moves the marker a number of times equivalent to the parameter.
     */
    public void moveMarker(int steps) {
        for (int i = 0; i < steps; i++) {
            moveMarker();
        }
    }

    /**
     * Moves the Faith Marker up by one position on the Faith Track.
     * When landing on a Pope's Place (with inactive status) a Vatican
     * Report starts.
     */
    private void moveMarker() {
        faithMarkerPosition++;
        if (checkVaticanReport(faithMarkerPosition)){
            player.getGame().startVaticanReport(faithMarkerPosition / 8);
        }
    }

    /**
     * If the tile on the given position corresponds to a Pope Space and the
     * corresponding Vatican Report has not been called yet, method returns true.
     * Otherwise returns false.
     * @param pos position on the Faith Track of the tile that will be checked.
     * @return outcome of the checks on the tile.
     */
    protected boolean checkVaticanReport(int pos) {
        // Pope Spaces are located on tiles with positions multiples of 8
        return (pos % 8) == 0 && popesFavors.get(pos / 8 - 1) == FavorStatus.INACTIVE;
    }

    /**
     * Handles the logic for turning or discarding a Pope's Favor
     * depending on the Faith Cross' position.
     * @param index indicates which of the three Pope's Favors has
     *              to be evaluated. If an integer different from 1, 2
     *              or 3 gets passed undefined behavior may occur.
     */
    public void flipPopesFavor(int index) {
        // The 3 Vatican Report sections are sized differently, the first
        // one being 4 tiles long and the second and third respectively
        // 5 and 6.
        int vaticanReportSectionSize = 3 + index;
        // If the Faith Marker is inside the Vatican Report Section then
        // the Favor gets activated.
        if(faithMarkerPosition > (index * 8) - vaticanReportSectionSize) {
            popesFavors.set(index - 1, FavorStatus.ACTIVE);
        } else {
            popesFavors.set(index - 1, FavorStatus.DISCARDED);
        }
    }
}