package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.DiscardLeaderCardAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiscardLeaderCardActionTest {
    PersonalBoard board;
    DiscardLeaderCardAction discardLeaderCardAction;
    LeaderCard leaderCard;

    @BeforeEach
    void init(){
        leaderCard = new StoreLeaderCard(3, new LeaderCardRequirements(null, null), Resource.SERVANT);
        Game game = new Game(1, 1);
        Player player = new Player("Nick", game);
        board = new PersonalBoard(player);
        discardLeaderCardAction = new DiscardLeaderCardAction(board, leaderCard);
    }
    @Test
    @DisplayName("Faith marker moved by one")
    void faithIncrementByOneTest() {
        int initialFaithMarkerPosition = board.getFaithTrack().getFaithMarkerPosition();
        discardLeaderCardAction.forward();
        assertEquals(initialFaithMarkerPosition+1, board.getFaithTrack().getFaithMarkerPosition());
    }

    @Test
    @DisplayName("The right LeaderCard has been discarded")
    void discardedRightLeaderCardTest() {
        board.setLeaderCards(List.of(leaderCard));
        discardLeaderCardAction.forward();
        assertFalse(board.getLeaderCards().contains(leaderCard));
    }

    @Test
    @DisplayName("Only the right LeaderCard has been discarded")
    void discardedOnlyTheRightLeaderCardTest() {
        LeaderCard leaderCard1 = new DiscountLeaderCard(3, new LeaderCardRequirements(null, null), Resource.STONE);
        board.setLeaderCards(List.of(leaderCard, leaderCard1));
        discardLeaderCardAction.forward();
        assertAll(
                ()->assertTrue(board.getLeaderCards().contains(leaderCard1)),
                ()->assertEquals(1, board.getLeaderCards().size())
        );
    }
}
