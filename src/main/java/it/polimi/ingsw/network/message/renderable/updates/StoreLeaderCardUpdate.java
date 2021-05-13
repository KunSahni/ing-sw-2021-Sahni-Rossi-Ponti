package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;

/**
 * This class contains an update regarding a store leader card whose stored resources were changed
 */
public class StoreLeaderCardUpdate extends BroadcastRenderable {
    private final String nickname;
    private final DumbStoreLeaderCard leaderCard;

    public StoreLeaderCardUpdate(String nickname, StoreLeaderCard leaderCard) {
        this.nickname = nickname;
        this.leaderCard = new DumbStoreLeaderCard(leaderCard);
    }


    @Override
    public void render(UI ui) {
        ui.updateStoreLeaderCardStorage(nickname, leaderCard);
    }
}
