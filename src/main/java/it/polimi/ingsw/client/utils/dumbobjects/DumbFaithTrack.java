package it.polimi.ingsw.client.utils.dumbobjects;

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

    //todo: meglio fare anche DumbSinglePlayerFaithTrack o lo gestisco da qui?

    public DumbFaithTrack() {
        super();
    }

    public void updateFaithMarkerPosition(int faithMarkerPosition) {
        this.faithMarkerPosition = faithMarkerPosition;
    }

    public void updatePopesFavors(List<FavorStatus> popesFavors) {
        this.popesFavors = new ArrayList<>(popesFavors);
    }

    public int getFaithMarkerPosition() {
        return faithMarkerPosition;
    }

    public List<FavorStatus> getPopesFavors() {
        return popesFavors;
    }
}
