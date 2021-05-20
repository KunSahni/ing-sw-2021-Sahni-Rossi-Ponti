package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains an updated version of a player's DevelopmentCardSlot which will be saved in the local DumbModel
 */
public class DevelopmentCardSlotUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int position;
    private final List<DumbDevelopmentCard> updatedDevelopmentCardSlot;

    public DevelopmentCardSlotUpdate(String nickname, DevelopmentCardSlot updatedDevelopmentCardSlot) {
        this.nickname = nickname;
        this.position = updatedDevelopmentCardSlot.getSlotIndex();
        this.updatedDevelopmentCardSlot = updatedDevelopmentCardSlot.
                getDevelopmentCards()
                .stream()
                .map(
                    DumbDevelopmentCard::new
                )
                .collect(Collectors.toList());
    }

    @Override
    public void render(UI ui) {
        ui.updateDevelopmentCardSlot(nickname, position, updatedDevelopmentCardSlot);
    }
}
