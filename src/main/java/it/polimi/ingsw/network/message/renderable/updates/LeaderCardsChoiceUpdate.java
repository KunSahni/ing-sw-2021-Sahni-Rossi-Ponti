package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;

import java.util.List;

/**
 * This class contains an update regarding the leader cards from which a player can choose
 */
public class LeaderCardsChoiceUpdate extends PrivateRenderable {
    private final List<DumbLeaderCard> leaderCards;

    public LeaderCardsChoiceUpdate(String nickname, List<DumbLeaderCard> leaderCards) {
        super(nickname);
        this.leaderCards = leaderCards;
    }

    @Override
    public void render(UI ui) {
        ui.renderLeaderCardsChoice(leaderCards);
    }
}
