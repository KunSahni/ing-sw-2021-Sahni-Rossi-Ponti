package it.polimi.ingsw.server.network.utils.dumbobjects;

import it.polimi.ingsw.server.model.personalboardpackage.FaithTrack;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a dumber version of a regular FaithTrack,
 * this class only contains data contained in a FaithTrack but has none of its logic.
 */
public class DumbFaithTrack {
    private int faithMarkerPosition;
    private List<FavorStatus> popesFavors;

    public DumbFaithTrack(FaithTrack faithTrack) {
        this.faithMarkerPosition = faithTrack.getFaithMarkerPosition();
        this.popesFavors = new ArrayList<>(faithTrack.getPopesFavors());
    }

    public int getFaithMarkerPosition() {
        return faithMarkerPosition;
    }

    public List<FavorStatus> getPopesFavors() {
        return popesFavors;
    }
}
