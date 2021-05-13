package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.personalboardpackage.FaithTrack;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;

import java.util.List;

/**
 * This class contains an update regarding a player's faith track which will be saved in the local DumbModel
 */
public class FaithTrackUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int updatedFaithTrackPosition;
    private final List<FavorStatus> updatedPopesFavors;

    public FaithTrackUpdate(String nickname, FaithTrack updatedFaithTrack) {
        this.nickname = nickname;
        this.updatedFaithTrackPosition = updatedFaithTrack.getFaithMarkerPosition();
        this.updatedPopesFavors = updatedFaithTrack.getPopesFavors();
    }

    @Override
    public void render(UI ui) {
        ui.updateFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors);
    }
}
