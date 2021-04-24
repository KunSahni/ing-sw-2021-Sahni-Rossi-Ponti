package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.message.action.BuyDevelopmentCardAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BuyDevelopmentCardActionTest {
    BuyDevelopmentCardAction buyDevelopmentCardAction;
    Turn turn;
    Game game;
    Player player;
    DevelopmentCard developmentCard;

    @BeforeEach
    void init(){
        game = new Game(1, 1);
        player = new Player("Nick", game);
        turn = new Turn(game, player);
        player.getPersonalBoard().storeInStrongbox(Map.of(Resource.STONE, 2));
        developmentCard = game.getDevelopmentCardsBoard().peekCard(Level.LEVEL1, Color.GREEN);
        buyDevelopmentCardAction = new BuyDevelopmentCardAction(turn, Level.LEVEL1, Color.GREEN, 1, null, Map.of(Resource.STONE, 1));
    }

    @Test
    @DisplayName("Development card just bought is on the personal board of the player in the right position")
    void DevelopmentCardAddedInRightPositionTest(){
        buyDevelopmentCardAction.forward();
        assertTrue(player.getPersonalBoard().getDevelopmentCardSlots().get(1).getDevelopmentCards().contains(developmentCard));
    }

    @Test
    @DisplayName("DevelopmentCard price has been paid")
    void DevelopmentCardPricePaidTest() {
        int initialStoneReserve = player.getPersonalBoard().getResources().get(Resource.STONE);
        buyDevelopmentCardAction.forward();
        assertEquals(initialStoneReserve-1, player.getPersonalBoard().getResources().get(Resource.STONE));
    }
}
