package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.controller.message.choice.LeaderCardsChoiceMessage;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardChoiceMessageTest {
    @Test
    void LeaderChoiceMessageTest(){
        List<LeaderCard> list = new ArrayList<>();
        LeaderCard leaderCard = new DiscountLeaderCard(1, new LeaderCardRequirements(null, null), Resource.SHIELD);
        LeaderCard leaderCard1 = new StoreLeaderCard(2, new LeaderCardRequirements(null, null), Resource.COIN);
        LeaderCard leaderCard2 = new StoreLeaderCard(3, new LeaderCardRequirements(null, null), Resource.SERVANT);
        LeaderCard leaderCard3 = new DiscountLeaderCard(3, new LeaderCardRequirements(null, null), Resource.STONE);
        list.add(leaderCard);
        list.add(leaderCard1);
        list.add(leaderCard2);
        list.add(leaderCard3);
        LeaderCardsChoiceMessage leaderCardsChoiceMessage = new LeaderCardsChoiceMessage(list);

        assertEquals(list, leaderCardsChoiceMessage.getLeaderCards());
    }
}
