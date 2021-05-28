package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PregameResourceChoiceActionTest {
    PregameResourceChoiceAction pregameResourceChoiceAction;
    Game game;
    String nick1;
    String nick2;
    Server server;

    @BeforeAll
    static void deleteActions(){
        deleteDir();
        new File("src/main/resources/games").mkdirs();
    }

    public void init(Integer gameId){
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        try {
            game = new Game(server, gameId, List.of(nick1, nick2));
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
            init(47);
            pregameResourceChoiceAction.execute();
            assertTrue(game.getPlayer(nick1).getPersonalBoard().depotsCanContain(Map.of(Resource.COIN, 1)));
        }

        @Test
        @DisplayName("All players have received their resources and game state is changed")
        void setNewStateTest() {
            init(48);
            pregameResourceChoiceAction.execute();
            assertEquals(GameState.PICKED_RESOURCES, game.getCurrentState());
        }

        @Test
        @DisplayName("Not all players have still received their resources, so the game doesn't start")
        void name() {
            init(49);
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
            init(50);
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
            init(51);
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
            init(52);
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
            init(53);
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

    static void deleteDir() {
        try {
            Files.walk(Path.of("src/main/resources/games"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @AfterEach
    void delete(){
        deleteDir();
    }
}
