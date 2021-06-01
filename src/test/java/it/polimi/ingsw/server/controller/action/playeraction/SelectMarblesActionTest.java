package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class SelectMarblesActionTest {
    SelectMarblesAction selectMarblesAction;
    Game game;
    String nick1;
    String nick2;
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
        game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
        selectMarblesAction = new SelectMarblesAction(Map.of(MarketMarble.BLUE, 1));
        selectMarblesAction.setNickname(nick1);
        selectMarblesAction.setGame(game);
        game.getPlayer(nick1).setTempMarbles(Map.of(MarketMarble.BLUE, 2));
    }

    @Nested
    @DisplayName("Tests for execute method") //todo: why execute return type is GameAction but it actually returns always null?
    class executeTests {

        @Test
        @DisplayName("Resources have been added correctly")
        void marblesAddedTest() {
            selectMarblesAction.execute();
            assertTrue(game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().contains(Map.of(Resource.SHIELD, 1)));
        }

        @Test
        @DisplayName("Other players marker have been moved")
        void otherMarkersMovedTest() {
            selectMarblesAction.execute();
            assertEquals(1, game.getPlayer(nick2).getPersonalBoard().getFaithTrack().getFaithMarkerPosition());
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            try {
                selectMarblesAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("Player can't perform this  action at this time")
        void actionInWrongTime() {
            game.getPlayer(nick1).addAction(ExecutedActions.STORED_MARKET_RESOURCES_ACTION);

            try {
                selectMarblesAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot store market resources at this time.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Player has selected an illegal amount of marbles")
        void invalidAmountOfMarblesTest() {
            SelectMarblesAction selectMarblesAction1 = new SelectMarblesAction(Map.of(MarketMarble.BLUE, 3));
            selectMarblesAction1.setNickname(nick1);
            selectMarblesAction1.setGame(game);

            try {
                selectMarblesAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("The marbles you have supplied are not an acceptable" +
                        " subset of the marbles you have taken from the market.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Player's depots are full")
        void fullDepotsTest() {
            game.getPlayer(nick1).getPersonalBoard().storeInDepots(Map.of(Resource.SHIELD, 3, Resource.COIN, 2, Resource.STONE, 1));

            try {
                selectMarblesAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Your depots cannot contain the selected resources.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        //todo: in tempMarblesContainSelectedMarbles method by default a red marble is removed from player's temp marble
    }

    @Test
    void convertTest() {
        ConvertLeaderCard convertLeaderCard = new ConvertLeaderCard(1, new LeaderCardRequirements(null, null), Resource.COIN);
        ConvertLeaderCard convertLeaderCard1 = new ConvertLeaderCard(1, new LeaderCardRequirements(null, null), Resource.SERVANT);
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(convertLeaderCard, convertLeaderCard1));
        game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(convertLeaderCard);
        game.getPlayer(nick1).setTempMarbles(Map.of(MarketMarble.BLUE, 2, MarketMarble.WHITE, 2));
        SelectMarblesAction selectMarblesAction1 = new SelectMarblesAction(Map.of(MarketMarble.BLUE, 1, MarketMarble.YELLOW, 1, MarketMarble.PURPLE, 1));
        selectMarblesAction1.setNickname(nick1);
        selectMarblesAction1.setGame(game);

        try {
            selectMarblesAction1.runChecks();
            selectMarblesAction1.execute();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
        assertAll(
                () -> assertTrue(game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().contains(Map.of(Resource.SHIELD, 1))),
                () -> assertTrue(game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().contains(Map.of(Resource.COIN, 1))),
                () -> assertTrue(game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().contains(Map.of(Resource.SERVANT, 1)))
        );

    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }
}
