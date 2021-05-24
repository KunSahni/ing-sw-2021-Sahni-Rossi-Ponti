package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboard.PersonalBoard;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;

    @BeforeEach
    void init() throws IOException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        Game game = new Game(null,1, nicknames);
        player = game.getPlayer("Mario");
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
            assertTrue(player.isConnected(), "Error: method returned false even though player is connected");
        }

        @Test
        @DisplayName("disconnect method test")
        void disconnectTest(){
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

    @Nested
    @DisplayName("Actions related methods' testing")
    class ActionTest{
        List<ExecutedActions> executedActions;

        @BeforeEach
        void setUp(){
            executedActions.add(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
            executedActions.add(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
            player.addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
            player.addAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
        }

        @Test
        @DisplayName("addAction method test")
        void addActionTest() {
            assertEquals(executedActions, player.getPerformedActions(), "Error: player doesn't contain the correct executed actions");
        }

        @Test
        @DisplayName("isValidNextAction method test")
        void isValidNextActionTest() {
            assertAll(
                    () -> assertFalse(player.isValidNextAction(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION), "Error: method returned true on an action which is not valid"),
                    () -> assertFalse(player.isValidNextAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION), "Error: method returned true on an action which is not valid"),
                    () -> assertTrue(player.isValidNextAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION), "Error: method returned false on an action which is valid"),
                    () -> assertTrue(player.isValidNextAction(ExecutedActions.TURN_ENDED_ACTION), "Error: method returned false on an action which is valid")
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
    void compareToTest() throws IOException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        Game game = new Game(null,1, nicknames);
        Player player2 = game.getPlayer("Luigi");

        assertAll(
                () -> assertEquals(0, player.compareTo(player), "Error: compareTo returned value different than 0 on same object"),
                () -> assertNotEquals(0, player.compareTo(player2), "Error: compareTo returned 0 on different objects")
        );
    }
}
