package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.controller.message.action.LeaderCardsChoiceAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardsChoiceActionTest {
    LeaderCardsChoiceAction leaderCardsChoiceAction;
    Player player;
    List<LeaderCard> pickedLeaderCards;

    @BeforeEach
    void init() {
        Game game = new Game(1, 1);
        player = new Player("Nick", game);
        pickedLeaderCards = game.getLeaderCardsDeck().popFour().subList(0, 2);
        leaderCardsChoiceAction = new LeaderCardsChoiceAction(pickedLeaderCards, player);
    }

    @Test
    @DisplayName("Player goes from having none to two LeaderCards")
    void twoLeaderCardsIncreaseTest() {
        List<LeaderCard> initialList = player.getPersonalBoard().getLeaderCards();
        leaderCardsChoiceAction.forward();
        assertAll(
                () -> assertEquals(0, initialList.size()),
                () -> assertEquals(2, player.getPersonalBoard().getLeaderCards().size())
        );
    }

    @Test
    @DisplayName("LeaderCards have been correctly stored")
    void correctlyStoredTest() {
        leaderCardsChoiceAction.forward();
        assertEquals(pickedLeaderCards, player.getPersonalBoard().getLeaderCards());
    }
}
