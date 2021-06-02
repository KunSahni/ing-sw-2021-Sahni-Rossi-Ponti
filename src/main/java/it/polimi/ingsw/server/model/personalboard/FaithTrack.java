package it.polimi.ingsw.server.model.personalboard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;

/**
 * Class representing a standard Faith Track used in a multi-player match.
 */
public class FaithTrack implements VictoryPointsElement {
    private int faithMarkerPosition;
    private final List<FavorStatus> popesFavors;
    protected transient ChangesHandler changesHandler;
    protected transient String nickname;

    protected FaithTrack() {
        this.popesFavors = new ArrayList<>();
    }

    public void init(String nickname, ChangesHandler changesHandler) {
        this.nickname = nickname;
        this.changesHandler = changesHandler;
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
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/json/default/FaithTrackPoints.json"));
            Map<String, Double> victoryPointsMap = new Gson().fromJson(reader, Map.class);
            return victoryPointsMap.get(Integer.toString(getFaithMarkerPosition())).intValue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Moves the Faith Marker up by one position on the Faith Track.
     */
    public void moveMarker() {
        if (faithMarkerPosition < 24) {
            faithMarkerPosition++;
            changesHandler.writeFaithTrack(nickname,this);
        }
    }

    /**
     * If the tile on which the FaithMarker is placed corresponds to a Pope Space
     * and the corresponding Vatican Report has not been called yet, method returns true.
     * Otherwise returns false.
     * @return outcome of the checks on the tile.
     */
    public boolean checkVaticanReport(int tile) {
        // Pope Spaces are located on tiles with positions multiples of 8
        if ((tile % 8) == 0 && tile > 0)
            return popesFavors.get(tile / 8 - 1) == FavorStatus.INACTIVE;
        else return false;
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
        changesHandler.writeFaithTrack(nickname, this);
    }
}