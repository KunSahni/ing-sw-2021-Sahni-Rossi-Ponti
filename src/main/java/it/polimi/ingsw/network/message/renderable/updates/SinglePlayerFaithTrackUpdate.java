package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;

import java.util.List;

/**
 * This class contains an update regarding the black cross's position on the faith track
 */
public class SinglePlayerFaithTrackUpdate extends PrivateRenderable {
    private final int updatedFaithTrackPosition;
    private final List<FavorStatus> updatedPopesFavors;
    private final int updatedBlackCrossPosition;

    public SinglePlayerFaithTrackUpdate(String nickname, SinglePlayerFaithTrack updatedFaithTrack) {
        super(nickname);
        this.updatedFaithTrackPosition = updatedFaithTrack.getFaithMarkerPosition();
        this.updatedPopesFavors = updatedFaithTrack.getPopesFavors();
        this.updatedBlackCrossPosition = updatedFaithTrack.getBlackCrossPosition();
    }

    @Override
    public void render(UI ui) {
        ui.updateSinglePlayerFaithTrack(updatedFaithTrackPosition, updatedPopesFavors, updatedBlackCrossPosition);
    }
}
