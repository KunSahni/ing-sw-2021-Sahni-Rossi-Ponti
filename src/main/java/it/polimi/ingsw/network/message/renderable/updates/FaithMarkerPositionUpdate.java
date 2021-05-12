package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 * This class contains an update regarding a player's FaithMarkerPosition which will be saved in the local DumbModel
 */
public class FaithMarkerPositionUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int updatedFaithTrackPosition;

    public FaithMarkerPositionUpdate(String nickname, int updatedFaithTrackPosition) {
        this.nickname = nickname;
        this.updatedFaithTrackPosition = updatedFaithTrackPosition;
    }

    @Override
    public void render(UI ui) {
        ui.updateFaithTrackPosition(nickname, updatedFaithTrackPosition);
    }
}
