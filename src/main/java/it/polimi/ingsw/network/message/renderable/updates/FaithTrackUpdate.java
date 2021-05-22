package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import it.polimi.ingsw.server.model.personalboard.SinglePlayerFaithTrack;

import java.util.List;

/**
 * This class contains an update regarding a player's faith track which will be saved in the local DumbModel
 */
public class FaithTrackUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int updatedFaithTrackPosition;
    private final List<FavorStatus> updatedPopesFavors;
    private final int updatedBlackCrossPosition;

    public FaithTrackUpdate(String nickname, FaithTrack updatedFaithTrack) {
        this.nickname = nickname;
        this.updatedFaithTrackPosition = updatedFaithTrack.getFaithMarkerPosition();
        this.updatedPopesFavors = updatedFaithTrack.getPopesFavors();
        this.updatedBlackCrossPosition = -1;
    }

    public FaithTrackUpdate(String nickname, SinglePlayerFaithTrack updatedFaithTrack) {
        this.nickname = nickname;
        this.updatedFaithTrackPosition = updatedFaithTrack.getFaithMarkerPosition();
        this.updatedPopesFavors = updatedFaithTrack.getPopesFavors();
        this.updatedBlackCrossPosition = updatedFaithTrack.getBlackCrossPosition();
    }

    @Override
    public void render(UI ui) {
        //if multiplayer game then this is a faith track
        if(updatedBlackCrossPosition == -1)
            ui.updateFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors);
        //else this is a single player faith track
        else
            ui.updateSinglePlayerFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors, updatedBlackCrossPosition);
    }
}
