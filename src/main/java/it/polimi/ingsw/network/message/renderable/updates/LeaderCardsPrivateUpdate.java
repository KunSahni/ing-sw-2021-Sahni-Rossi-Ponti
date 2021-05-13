package it.polimi.ingsw.network.message.renderable.updates;

import com.sun.source.tree.BreakTree;
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
        this.updatedLeaderCards = updatedLeaderCards.stream().map(
                leaderCard -> {
                    switch (leaderCard.getAbility()){
                        case DISCOUNT -> {
                            return new DumbDiscountLeaderCard((DiscountLeaderCard) leaderCard);
                        }
                        case STORE -> {
                            return new DumbStoreLeaderCard((StoreLeaderCard) leaderCard);
                        }
                        case CONVERT -> {
                            return new DumbConvertLeaderCard((ConvertLeaderCard) leaderCard);
                        }
                        case PRODUCE -> {
                            return new DumbProduceLeaderCard((ProduceLeaderCard) leaderCard);
                        }
                        default -> {
                            return null;
                        }
                    }
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void render(UI ui) {
        ui.updateLeaderCards(updatedLeaderCards);
    }
}