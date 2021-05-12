package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

import java.util.List;

/**
 * This class contains an update regarding a player's leader cards which will be saved in the local DumbModel
 */
public class LeaderCardsUpdate extends BroadcastRenderable {
    private final String nickname;
    private final List<DumbLeaderCard> updatedLeaderCards;

    public LeaderCardsUpdate(String nickname, List<DumbLeaderCard> updatedLeaderCards) {
        this.nickname = nickname;
        this.updatedLeaderCards = updatedLeaderCards;
    }

    @Override
    public void render(UI ui) {
        ui.updateLeaderCards(nickname, updatedLeaderCards);
    }
}
