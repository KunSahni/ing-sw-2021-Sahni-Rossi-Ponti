package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

import java.util.List;

/**
 * This class contains an update regarding this player's leader cards which will be saved in the local DumbModel
 */
public class LeaderCardsPrivateUpdate extends PrivateRenderable {
    private final List<DumbLeaderCard> updatedLeaderCards;

    public LeaderCardsPrivateUpdate(String nickname, List<DumbLeaderCard> updatedLeaderCards) {
        this.updatedLeaderCards = updatedLeaderCards;
    }

    @Override
    public void render(UI ui) {
        ui.updateLeaderCards(updatedLeaderCards);
    }
}