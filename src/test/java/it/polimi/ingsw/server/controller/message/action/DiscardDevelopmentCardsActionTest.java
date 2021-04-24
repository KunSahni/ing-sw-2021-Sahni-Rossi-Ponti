package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.message.action.DiscardDevelopmentCardsAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscardDevelopmentCardsActionTest {

    DiscardDevelopmentCardsAction discardDevelopmentCardsAction;
    Turn turn;
    Game game;
    Player player;

    @BeforeEach
    void init(){
        game = new Game(1, 1);
        player = new Player("Nick", game);
        turn = new Turn(game, player);
        discardDevelopmentCardsAction = new DiscardDevelopmentCardsAction(turn, ActionToken.REMOVEBLUE);
    }

    @Test
    @DisplayName("The right two development card has been discarted")
    void discardedTwoDevelopmentCardsTest(){
        DevelopmentCard discardedCard1 = game.getDevelopmentCardsBoard().peekBoard()[2][1].pop();
        DevelopmentCard discardedCard2 = game.getDevelopmentCardsBoard().peekBoard()[2][1].pop();
        discardDevelopmentCardsAction.forward();
        assertAll(
                ()->assertFalse(game.getDevelopmentCardsBoard().peekBoard()[2][1].getDeck().contains(discardedCard1)),
                ()->assertFalse(game.getDevelopmentCardsBoard().peekBoard()[2][1].getDeck().contains(discardedCard2))
        );
    }

    @Test
    @DisplayName("Only the two right development cards have been discarded")
    void discardedOnlyTheTwoRightDevelopmentCardsTest() {
        DevelopmentCardsDeck[][] expectedDevelopmentCards = game.getDevelopmentCardsBoard().peekBoard();
        expectedDevelopmentCards[2][1].pop();
        expectedDevelopmentCards[2][1].pop();
        discardDevelopmentCardsAction.forward();
        //assertEquals(developmentCards, game.getDevelopmentCardsBoard().peekBoard());
        assertAll(
                ()-> assertEquals(expectedDevelopmentCards[0][0], game.getDevelopmentCardsBoard().peekBoard()[0][0]),
                ()-> assertEquals(expectedDevelopmentCards[0][1], game.getDevelopmentCardsBoard().peekBoard()[0][1]),
                ()-> assertEquals(expectedDevelopmentCards[0][2], game.getDevelopmentCardsBoard().peekBoard()[0][2]),
                ()-> assertEquals(expectedDevelopmentCards[0][3], game.getDevelopmentCardsBoard().peekBoard()[0][3]),
                ()-> assertEquals(expectedDevelopmentCards[1][0], game.getDevelopmentCardsBoard().peekBoard()[1][0]),
                ()-> assertEquals(expectedDevelopmentCards[1][1], game.getDevelopmentCardsBoard().peekBoard()[1][1]),
                ()-> assertEquals(expectedDevelopmentCards[1][2], game.getDevelopmentCardsBoard().peekBoard()[1][2]),
                ()-> assertEquals(expectedDevelopmentCards[1][3], game.getDevelopmentCardsBoard().peekBoard()[1][3]),
                ()-> assertEquals(expectedDevelopmentCards[2][0], game.getDevelopmentCardsBoard().peekBoard()[2][0]),
                ()-> assertEquals(expectedDevelopmentCards[2][1], game.getDevelopmentCardsBoard().peekBoard()[2][1]),
                ()-> assertEquals(expectedDevelopmentCards[2][2], game.getDevelopmentCardsBoard().peekBoard()[2][2]),
                ()-> assertEquals(expectedDevelopmentCards[2][3], game.getDevelopmentCardsBoard().peekBoard()[2][3])
        );
    }
}
