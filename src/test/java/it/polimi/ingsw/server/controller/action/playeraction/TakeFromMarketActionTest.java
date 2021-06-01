package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class TakeFromMarketActionTest {

    TakeFromMarketAction takeFromMarketAction;
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
        game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
        takeFromMarketAction = new TakeFromMarketAction(1, true);
        takeFromMarketAction.setNickname(nick1);
        takeFromMarketAction.setGame(game);
    }

    @Nested
    @DisplayName("Tests for execute method") //todo: why execute type is GameAction but it actually returns always null?
    class executeTests {

        @Test
        @DisplayName("Marbles have been added correctly")
        void marblesAddedTest() {
            takeFromMarketAction.execute();
            assertNotNull(game.getPlayer(nick1).getTempMarbles());
        }

        @Test
        @DisplayName("Action has been added correctly")
        void actionAddedTest() {
            takeFromMarketAction.execute();
            assertTrue(game.getPlayer(nick1).getPerformedActions().contains(ExecutedActions.STORED_TEMP_MARBLES_ACTION));
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            try {
                takeFromMarketAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("A player can't perform an action not during his turn")
        void invalidTurnTest() {
            game.getPlayer(nick1).finishTurn();
            try {
                takeFromMarketAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Not your turn")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Invalid action is rejected")
        void invalidActionTest() {
            game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
            try {
                takeFromMarketAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot take from market at this time")){
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
