package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.utils.GameState;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Game game;
    ArrayList<String> nicknames;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp() throws IOException {
        nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        changesHandler = new ChangesHandler(1);
        Server server = new Server();
        game = new Game(server, 1, nicknames);
    }

    @AfterEach
    void tearDown() {
        changesHandler.publishGameOutcome(game);
    }

    @Test
    void gameRecreationTest() throws IOException {
        game = new Game(new Server(), 1 , null);
        List<String> new_nicknames = game.getPlayerList()
                .stream()
                .map(
                        Player::getNickname
                )
                .collect(Collectors.toList());
        assertEquals(new_nicknames, nicknames, "game recreation didn't work properly");

    }

    @Test
    @DisplayName("size method test")
    void sizeTest() {
        assertEquals(nicknames.size(), game.size(), "Error: game returned a wrong size");
    }

    @Test
    @DisplayName("getPlayerList")
    void getPlayerListTest() {
        List<String> actualNicknames = game.getPlayerList().stream().map(
                Player::getNickname
        ).collect(Collectors.toList());
        assertTrue(actualNicknames.containsAll(nicknames), "Error: game does not contain the inserted players");
    }

    @Test
    @DisplayName("getPlayer test")
    void getPlayerTest() {
        assertAll(
                () -> assertNotNull(game.getPlayer("Mario"))
                //() -> assertEquals(changesHandler.readPlayer("Mario"), game.getPlayer("Mario"))
        );
    }

    @Test
    @DisplayName("getCurrentTurnPlayer method test")
    void getCurrentTurnPlayerTest() {
        game.getPlayer("Mario").startTurn();
        assertEquals(game.getPlayer("Mario"), game.getCurrentTurnPlayer(), "Error: game did not return correct player");
    }

    @Test
    @DisplayName("sortPlayers method test")
    void sortPlayersTest() {
        game.getPlayer("Mario").setPosition(0);
        game.getPlayer("Luigi").setPosition(1);
        game.sortPlayers();
        assertAll(
                () -> assertEquals(game.getPlayer("Mario"), game.getPlayerList().get(0), "Error: game didn't properly sort the players"),
                () -> assertEquals(game.getPlayer("Luigi"), game.getPlayerList().get(1), "Error: game didn't properly sort the players")
        );
    }

    @Test
    @DisplayName("connect method test")
    void connectTest() {
        game.connect("Mario");
        assertTrue(game.getPlayer("Mario").isConnected(), "Error: game didn't properly connect player");
    }

    @Test
    @DisplayName("disconnect method test")
    void disconnectTest() {
        game.disconnect("Mario");
        assertFalse(game.getPlayer("Mario").isConnected(), "Error: game didn't properly disconnect player");
    }

    @Test
    void endTest() {
        game.end();
        assertFalse(Files.exists(Paths.get("src/main/resources/games/1")), "Error: game did not properly remove directory");
    }

    @Test
    @DisplayName("setState method test")
    void setStateTest() {
        game.setState(GameState.IN_GAME);
        assertEquals(GameState.IN_GAME, game.getCurrentState(), "Error: game is not in the right state");
    }

    @Nested
    @DisplayName("getMarket method tests")
    class getMarketTests {
        Market market;

        @BeforeEach
        void init() {
            market = game.getMarket();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(market);
        }

        @Test
        @DisplayName("Two calls on the same method return equal objects")
        void coherentReturnsTest() {
            assertEquals(game.getMarket(), game.getMarket());
        }
    }

    @Nested
    @DisplayName("getLeaderCardsDeck method tests")
    class getLeaderCardsDeckTests {
        LeaderCardsDeck leaderCardsDeck;

        @BeforeEach
        void init() {
            leaderCardsDeck = game.getLeaderCardsDeck();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(leaderCardsDeck);
        }

        @Test
        @DisplayName("Two calls on the same method return equal objects")
        void coherentReturnsTest() {
            assertEquals(game.getLeaderCardsDeck(), game.getLeaderCardsDeck());
        }
    }

    @Nested
    @DisplayName("getDevelopmentCardsBoard method tests")
    class getDevelopmentCardsBoardTests {
        DevelopmentCardsBoard developmentCardsBoard;

        @BeforeEach
        void init() {
            developmentCardsBoard = game.getDevelopmentCardsBoard();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(developmentCardsBoard);
        }

        @Test
        @DisplayName("Two calls on the same method return equal objects")
        void coherentReturnsTest() {
            assertEquals(game.getDevelopmentCardsBoard(), game.getDevelopmentCardsBoard());
        }
    }

    @Nested
    @DisplayName("getActionTokenDeck method tests")
    class getActionTokenDeckTests {
        ActionTokenDeck actionTokenDeck;

        @BeforeEach
        void init() throws IOException {
            Server server = new Server();
            nicknames = new ArrayList<>();
            nicknames.add("Mario");
            tearDown();
            game = new Game(server, 1, nicknames);
            actionTokenDeck = game.getActionTokenDeck();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(actionTokenDeck);
        }

        @Test
        @DisplayName("Two calls on the same method return equal objects")
        void coherentReturnsTest() {
            assertEquals(game.getActionTokenDeck(), game.getActionTokenDeck());
        }
    }

}
