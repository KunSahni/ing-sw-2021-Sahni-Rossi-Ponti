package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.leadercard.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains an update regarding this player's leader cards which will be saved in the local DumbModel
 */
public class LeaderCardsPrivateUpdate extends PrivateRenderable {
    private final List<DumbLeaderCard> updatedLeaderCards;

    public LeaderCardsPrivateUpdate(String nickname, List<LeaderCard> updatedLeaderCards) {
        super(nickname);
        this.updatedLeaderCards = updatedLeaderCards.stream()
                .map(LeaderCard::convertToDumb)
                .collect(Collectors.toList());
    }

    @Override
    public void render(UI ui) {
        ui.updateLeaderCards(updatedLeaderCards);
    }
}