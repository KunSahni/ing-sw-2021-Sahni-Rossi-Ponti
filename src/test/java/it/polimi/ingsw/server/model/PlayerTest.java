package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboard.PersonalBoard;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PlayerTest {
    Player player;
    Game game;

    @BeforeEach
    void init() throws IOException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        game = new Game(new Server(),1, nicknames);
        player = game.getPlayer("Mario");
    }

    @AfterEach
    void tearDown() {
        new ChangesHandler(1).publishGameOutcome(game);
    }

    @Test
    @DisplayName("setPosition method test")
    void setPositionTest() {
        player.setPosition(1);
        assertEquals(1, player.getPosition(), "Error: method returned a different position than expected");
    }

    @Test
    @DisplayName("getNickname method test")
    void getNicknameTest() {
        assertEquals("Mario", player.getNickname(), "Error: method returned returned different nickname than expected");
    }

    @Nested
    @DisplayName("getPersonalBoard method tests")
    class getPersonalBoardTests {
        PersonalBoard personalBoard;

        @BeforeEach
        void init() {
            personalBoard = player.getPersonalBoard();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(personalBoard);
        }

        @Test
        @DisplayName("Two calls on the same method return equal objects")
        void coherentReturnsTest() {
            assertEquals(player.getPersonalBoard(), player.getPersonalBoard());
        }
    }

    @Nested
    @DisplayName("Connection related methods' testing")
    class ConnectionTests{

        @BeforeEach
        void setUp(){
            player.connect();
        }

        @Test
        @DisplayName("connect method test")
        void connect() {
            player.connect();
            assertTrue(player.isConnected(), "Error: method returned false even though player is connected");
        }

        @Test
        @DisplayName("disconnect method test")
        void disconnectTest(){
            player.disconnect();
            assertFalse(player.isConnected(), "Error: method returned true even though player is disconnected");
        }
    }

    @Nested
    @DisplayName("Leader cards related methods' testing")
    class LeaderCardsTests{
        List<LeaderCard> pickedLeaderCards;

        @BeforeEach
        void setUp() throws FileNotFoundException {
            pickedLeaderCards = new ChangesHandler(1).readLeaderCardsDeck().popFour();
            player.setTempLeaderCards(pickedLeaderCards);
        }

        @Test
        @DisplayName("setTempLeaderCards method test")
        void setTempLeaderCardsTest() {
            assertEquals(pickedLeaderCards, player.getTempLeaderCards(), "Error: player contains a different list of temp leader cards");
        }

        @Test
        @DisplayName("chooseTwoLeaderCards method test")
        void chooseTwoLeaderCardsTest() {
            pickedLeaderCards.remove(3);
            pickedLeaderCards.remove(2);
            player.chooseTwoLeaderCards(pickedLeaderCards);
            assertAll(
                    () -> assertEquals(pickedLeaderCards, player.getPersonalBoard().getLeaderCards(), "Error: player did not properly forward leader card choice to personal board"),
                    () -> assertEquals(0, player.getTempLeaderCards().size(), "Error: player did not clear temp leader cards after choice")
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Actions related methods' testing")
    class ActionTest{
        List<ExecutedActions> executedActions;

        @BeforeEach
        void setUp(){
            executedActions = new ArrayList<>();
            executedActions.add(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
            executedActions.add(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
        }

        @Test
        @DisplayName("addAction method test")
        void addActionTest() {
            player.addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
            player.addAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
            assertEquals(executedActions, player.getPerformedActions(), "Error: player doesn't contain the correct executed actions");
        }

        @ParameterizedTest
        @EnumSource(ExecutedActions.class)
        void isValidNextActionSingleActionTest(ExecutedActions e) {
            player.finishTurn();
            player.startTurn();
            if(e.equals(ExecutedActions.TURN_ENDED_ACTION) || e.equals(ExecutedActions.STORED_MARKET_RESOURCES_ACTION))
                assertFalse(player.isValidNextAction(e), "action should not be valid");
            else
                assertTrue(player.isValidNextAction(e), "action should be valid");
        }

        @ParameterizedTest
        @MethodSource("legalActions")
        @DisplayName("isValidNextAction method test when true")
        void isValidNextActionTrueTest(ExecutedActions e1, ExecutedActions e2) {
            player.finishTurn();
            player.startTurn();
            player.addAction(e1);
            assertTrue(player.isValidNextAction(e2), "action should be valid");
        }

        private Stream<Arguments> legalActions() {
            return Stream.of(
                    arguments(ExecutedActions.ACTIVATED_PRODUCTION_ACTION, ExecutedActions.ACTIVATED_LEADER_CARD_ACTION),
                    arguments(ExecutedActions.ACTIVATED_PRODUCTION_ACTION, ExecutedActions.DISCARDED_LEADER_CARD_ACTION),
                    arguments(ExecutedActions.ACTIVATED_PRODUCTION_ACTION, ExecutedActions.TURN_ENDED_ACTION),
                    arguments(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION, ExecutedActions.ACTIVATED_PRODUCTION_ACTION),
                    arguments(ExecutedActions.DISCARDED_LEADER_CARD_ACTION, ExecutedActions.ACTIVATED_PRODUCTION_ACTION),
                    arguments(ExecutedActions.STORED_TEMP_MARBLES_ACTION, ExecutedActions.STORED_MARKET_RESOURCES_ACTION)
            );
        }

        @ParameterizedTest
        @MethodSource("illegalActions")
        @DisplayName("isValidNextAction method test when false")
        void isValidNextActionFalseTest(ExecutedActions e1, ExecutedActions e2) {
            player.finishTurn();
            player.startTurn();
            player.addAction(e1);
            assertFalse(player.isValidNextAction(e2), "action should not be valid");
        }

        private Stream<Arguments> illegalActions() {
            return Stream.of(
                    arguments(ExecutedActions.ACTIVATED_PRODUCTION_ACTION, ExecutedActions.STORED_TEMP_MARBLES_ACTION),
                    arguments(ExecutedActions.ACTIVATED_PRODUCTION_ACTION, ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION),
                    arguments(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION, ExecutedActions.ACTIVATED_PRODUCTION_ACTION),
                    arguments(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION, ExecutedActions.STORED_TEMP_MARBLES_ACTION),
                    arguments(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION, ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION)
                    );
        }
    }

    @Test
    @DisplayName("setTempMarbles method test")
    void setTempMarblesTest() throws FileNotFoundException {
        Market market = new ChangesHandler(1).readMarket();
        Map<MarketMarble, Integer> pickedMarbles = market.chooseColumn(1);
        player.setTempMarbles(pickedMarbles);
        assertEquals(pickedMarbles, player.getTempMarbles(), "Error: player contains a different map of marbles");
    }

    @Test
    @DisplayName("startTurn and finishTurn method test")
    void turnTests() {
        player.startTurn();
        assertTrue(player.isTurn(), "Error: method return false, but player should be in turn");
        player.finishTurn();
        assertFalse(player.isTurn(), "Error: method return true, but player should not be in turn");
    }

    @Test
    @DisplayName("compareTo method test")
    void compareToTest() {
        Player player2 = game.getPlayer("Luigi");
        player.setPosition(0);
        player2.setPosition(1);

        //todo: implement better assertions
        assertAll(
                () -> assertEquals(0, player.compareTo(player), "Error: compareTo returned value different than 0 on same object"),
                () -> assertNotEquals(0, player.compareTo(player2), "Error: compareTo returned 0 on different objects")
        );
    }
}
