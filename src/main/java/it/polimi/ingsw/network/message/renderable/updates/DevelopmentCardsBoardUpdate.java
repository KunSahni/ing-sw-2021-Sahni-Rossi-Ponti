package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

import java.util.List;

/**
 * This class contains an updated version of DevelopmentCardsBoard which will be saved in the local DumbModel
 */
public class DevelopmentCardsBoardUpdate extends BroadcastRenderable {
    private final DumbDevelopmentCard[] updatedDevelopmentCardsDeck;

    public DevelopmentCardsBoardUpdate(List<DumbDevelopmentCard> updatedDevelopmentCardsDeck) {
        this.updatedDevelopmentCardsDeck = updatedDevelopmentCardsDeck.toArray(new DumbDevelopmentCard[12]);
    }

    @Override
    public void render(UI ui) {
        ui.updateDevelopmentCardsBoard(updatedDevelopmentCardsDeck);
    }
}
