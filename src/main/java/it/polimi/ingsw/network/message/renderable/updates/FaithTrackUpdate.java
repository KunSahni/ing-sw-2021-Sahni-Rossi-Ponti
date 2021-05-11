package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.model.personalboardpackage.FaithTrack;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 *  This class contains an updated version of a FaithTrack which will be saved in the local DumbModel
 */
public class FaithTrackUpdate extends BroadcastRenderable {
    private final String nickname;
    private final FaithTrack updatedFaithTrack;

    public FaithTrackUpdate(String nickname, FaithTrack updatedFaithTrack) {
        this.nickname = nickname;
        this.updatedFaithTrack = updatedFaithTrack;
    }

    @Override
    public void render(UI ui) {
        ui.updateFaithTrack(nickname, updatedFaithTrack);
    }
}