package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TakeFromMarketActionTest {

    TakeFromMarketAction takeFromMarketAction;
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
            init(57);
            takeFromMarketAction.execute();
            assertNotNull(game.getPlayer(nick1).getTempMarbles());
        }

        @Test
        @DisplayName("Action has been added correctly")
        void actionAddedTest() {
            init(58);
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
            init(59);
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
            init(60);
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
            init(61);
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
