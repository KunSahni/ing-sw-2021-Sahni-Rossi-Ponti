package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class PregameResourceChoiceActionTest {
    PregameResourceChoiceAction pregameResourceChoiceAction;
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
        game.getPlayer(nick1).addAction(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION);
        pregameResourceChoiceAction = new PregameResourceChoiceAction(Map.of(Resource.COIN, 1));
        pregameResourceChoiceAction.setNickname(nick1);
        pregameResourceChoiceAction.setGame(game);
        game.setState(GameState.ASSIGNED_INKWELL);
        game.getPlayer(nick1).setPosition(3);
    }

    @Nested
    @DisplayName("Tests for execute method")
    class executeTests {
        @Test
        @DisplayName("All resources have been stored correctly")
        void rightResourcesStorageTest() {
            pregameResourceChoiceAction.execute();
            assertTrue(game.getPlayer(nick1).getPersonalBoard().depotsCanContain(Map.of(Resource.COIN, 1)));
        }

        @Test
        @DisplayName("All players have received their resources and game state is changed")
        void setNewStateTest() {
            pregameResourceChoiceAction.execute();
            assertEquals(GameState.ASSIGNED_INKWELL, game.getCurrentState());
        }

        @Test
        @DisplayName("Not all players have still received their resources, so the game doesn't start")
        void name() {
            game.getPlayer(nick2).setPosition(3);
            assertNull(pregameResourceChoiceAction.execute());
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            try {
                pregameResourceChoiceAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("Not allowed action is rejected")
        void notAllowedActionTimeTest() {
            game.setState(GameState.IN_GAME);
            try {
                pregameResourceChoiceAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot choose Pregame Resources at this time.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Resources are not given because player already have some resources")
        void alreadyPresentResourcesTest() {
            game.getPlayer(nick1).getPersonalBoard().getStrongbox().storeResources(Map.of(Resource.STONE, 1));

            try {
                pregameResourceChoiceAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You already have resources in your storages.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Only players that have the right obtain resources")
        void resourcesGivenToRightPlayersTest() {
            game.getPlayer(nick1).setPosition(1);

            try {
                pregameResourceChoiceAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Invalid number of resources supplied")){
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
