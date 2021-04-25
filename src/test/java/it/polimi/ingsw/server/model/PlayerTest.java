package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.message.action.*;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.*;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    Player player;
    Game game;

    @BeforeEach
    void init(){
        game = new Game(1, 1);
        player = new Player("Nick", game);
    }

    @Test
    @DisplayName("Leader Cards have been added")
    void addLeaderCardsTest() {
        List<LeaderCard> leaderCards = List.of(new ConvertLeaderCard(1, null, Resource.COIN), new StoreLeaderCard(1, null, Resource.SERVANT));
        player.addLeaderCards(leaderCards);
        assertEquals(leaderCards, player.getLeaderCards());
    }

    @Test
    @DisplayName("Leader Cards have been set in the personal board")
    void setLeaderCardsTest() {
        List<LeaderCard> leaderCards = List.of(new ConvertLeaderCard(1, null, Resource.COIN), new StoreLeaderCard(1, null, Resource.SERVANT));
        player.setLeaderCards(leaderCards);
        assertEquals(leaderCards, player.getPersonalBoard().getLeaderCards());
    }

    @Nested
    class setPositionTest{

        @Test
        @DisplayName("Position has been set")
        void positionSetTest() {
            player.setPosition(1);
            assertEquals(1, player.getPosition());
        }

        @Test
        @DisplayName("Position has been set and faith marker has been moved")
        void positionSuperiorTwoTest() {
            player.setPosition(3);
            assertEquals(1, player.getPersonalBoard().getFaithTrack().getFaithMarkerPosition());
        }
    }

    @Test
    void setTempMarblesTest() {
        Map<MarketMarble, Integer> expected = Map.of(MarketMarble.GREY, 1);
        player.setTempMarbles(expected);
        assertEquals(expected, player.getTempMarbles());
    }

    @Nested
    class availableNextStateClass{
        @BeforeEach
        void setDevelopmentCards(){
            player.getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 0);
            player.getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 1);
            player.getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 2);
        }
        @Nested
        class emptyPerformedActionClass{
            @Test
            @DisplayName("TAKERESOURCEACTION is available")
            void emptyPerformedActionTest() {
                List<Actions> actionsList = List.of(Actions.TAKERESOURCEACTION);
                assertEquals(actionsList, player.availableNextStates());
            }

            @Test
            @DisplayName("ACTIVATELEADERCARDACTION is available")
            void canActivateLeaderCardTest() {
                player.getPersonalBoard().setLeaderCards(List.of(new DiscountLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.STONE, 1)), Resource.STONE)));
                player.getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, null, null, null, 1), 1);
                player.getPersonalBoard().storeInStrongbox(Map.of(Resource.STONE, 1));
                assertTrue(player.availableNextStates().contains(Actions.ACTIVATELEADERCARDACTION));
            }

            @Test
            @DisplayName("DISCARDLEADERCARDACTION is available")
            void canDiscardLeaderCardTest() {
                player.getPersonalBoard().setLeaderCards(List.of(new DiscountLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.STONE, 1)), Resource.STONE)));
                assertTrue(player.availableNextStates().contains(Actions.DISCARDLEADERCARDACTION));
            }

            @Test
            @DisplayName("BUYDEVELOPMENTCARDACTION is available")
            void canAffordDevelopmentCardTest() {
                player.getPersonalBoard().storeInStrongbox(Map.of(Resource.STONE, 100, Resource.COIN, 100, Resource.SHIELD, 100, Resource.SERVANT, 100));
                assertTrue(player.availableNextStates().contains(Actions.BUYDEVELOPMENTCARDACTION));
            }

            @Test
            @DisplayName("ACTIVATEPRODUCTIONACTION is available")
            void canAffordProductionTest() {
                player.getPersonalBoard().storeInDepots(Map.of(Resource.STONE, 2));
                assertTrue(player.availableNextStates().contains(Actions.ACTIVATEPRODUCTIONACTION));
            }
        }

        @Test
        @DisplayName("After having performed compulsory action ENDACTION is available")
        void hasPerformedCompulsoryActionTest() {
            player.addAction(new TakeResourceAction(new Turn(game, player), player, 1, true));
            assertTrue(player.availableNextStates().contains(Actions.ENDACTION));
        }
    }
}
