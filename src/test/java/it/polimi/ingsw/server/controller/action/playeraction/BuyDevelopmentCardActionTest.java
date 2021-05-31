package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class BuyDevelopmentCardActionTest {
    BuyDevelopmentCardAction buyDevelopmentCardAction;
    Game game;
    String nick1;
    String nick2;
    Map<Resource, Integer> cardCost;
    Server server;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp(){
        changesHandler = new ChangesHandler(1);
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        try {
            game = new Game(server, 1, List.of(nick1, nick2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        game.getPlayer(nick1).startTurn();
        game.getPlayer(nick1).addAction(ExecutedActions.DISCARDED_LEADER_CARD_ACTION);
        cardCost = game.getDevelopmentCardsBoard().peekCard(Level.LEVEL1, Color.GREEN).getCost();
        buyDevelopmentCardAction = new BuyDevelopmentCardAction(Level.LEVEL1, Color.GREEN, 1, null, cardCost);
        buyDevelopmentCardAction.setNickname(nick1);
        buyDevelopmentCardAction.setGame(game);
        game.getPlayer(nick1).getPersonalBoard().getStrongbox().storeResources(cardCost);
    }

    @Test
    @DisplayName("Action has been executed correctly")
    void executeTest() {
        assertNull(buyDevelopmentCardAction.execute());
    }

    @Test
    void discountWorksTest() {
        DiscountLeaderCard discountLeaderCard = new DiscountLeaderCard(1, new LeaderCardRequirements(null, Map.of(Resource.SHIELD, 1)), Resource.SHIELD);
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(discountLeaderCard));
        game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(discountLeaderCard);

        try {
            buyDevelopmentCardAction.runChecks();
            throw new AssertionError("Exception was not thrown");
        } catch (InvalidActionException e) {
            if (!e.getMessage().equals("You did not pass the correct amount of resources to" +
                    " purchase the selected Development Card")){
                e.printStackTrace();
                throw new AssertionError("Wrong exception was thrown");
            }
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            try {
                buyDevelopmentCardAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("Player that try to do an action not during his turn is rejected")
        void wrongTurnTest(){
            game.getPlayer(nick1).finishTurn();
            game.getPlayer(nick2).startTurn();
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Not your turn")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Not allowed action is rejected")
        void notAllowedActionTest() {
            game.getPlayer(nick1).addAction(ExecutedActions.TURN_ENDED_ACTION);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot buy a Development Card at this time")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Cards of level and color specified are not available, so the action is rejected")
        void noMoreCardsAvailable() {
            game.getDevelopmentCardsBoard().discardTwo(Color.GREEN);
            game.getDevelopmentCardsBoard().discardTwo(Color.GREEN);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("GREEN LEVEL1Development Cards are not " +
                        "available on the board anymore.")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Wrong resources amount is passed so the action is rejected")
        void passedResourceDontMatchCostTest() {
            BuyDevelopmentCardAction buyDevelopmentCardAction1 = new BuyDevelopmentCardAction(Level.LEVEL1, Color.GREEN, 1, null, Map.of(Resource.COIN, 1));
            buyDevelopmentCardAction1.setNickname(nick1);
            buyDevelopmentCardAction1.setGame(game);
            try {
                buyDevelopmentCardAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You did not pass the correct amount of resources to" +
                        " purchase the selected Development Card")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("The player doesn't have enough resources, so the action is rejected")
        void notEnoughResourcesTest() {
            game.getPlayer(nick1).getPersonalBoard().discardFromStrongbox(cardCost);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Your storages do not have enough resources.")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("A player can't place a card if doesn't own a card of inferior level")
        void illegalCardPositionTest() {
            cardCost = game.getDevelopmentCardsBoard().peekCard(Level.LEVEL2, Color.GREEN).getCost();
            BuyDevelopmentCardAction buyDevelopmentCardAction1 = new BuyDevelopmentCardAction(Level.LEVEL2, Color.GREEN, 1, null, cardCost);
            buyDevelopmentCardAction1.setNickname(nick1);
            buyDevelopmentCardAction1.setGame(game);
            game.getPlayer(nick1).getPersonalBoard().getStrongbox().storeResources(cardCost);
            game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().storeResources(cardCost);
            try {
                buyDevelopmentCardAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot place a LEVEL2 Development Card  in the Development Cards Slot number 1")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }
}
