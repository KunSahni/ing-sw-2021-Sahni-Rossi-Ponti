package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains an updated version of a player's DevelopmentCardSlot which will be saved in the local DumbModel
 */
public class DevelopmentCardSlotUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int position;
    private final List<DumbDevelopmentCard> updatedDevelopmentCardSlot;

    public DevelopmentCardSlotUpdate(String nickname, int position, List<DumbDevelopmentCard> updatedDevelopmentCardSlot) {
        this.nickname = nickname;
        this.position = position;
        this.updatedDevelopmentCardSlot = new ArrayList<>(updatedDevelopmentCardSlot);
    }

    @Override
    public void render(UI ui) {
        ui.updateDevelopmentCardSlot(nickname, position, updatedDevelopmentCardSlot);
    }
}
