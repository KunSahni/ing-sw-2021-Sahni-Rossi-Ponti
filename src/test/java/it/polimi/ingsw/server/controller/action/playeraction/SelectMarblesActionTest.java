package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
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

public class SelectMarblesActionTest {
    SelectMarblesAction selectMarblesAction;
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
            init(54);
            selectMarblesAction.execute();
            assertTrue(game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().contains(Map.of(Resource.SHIELD, 1)));
        }

        @Test
        void otherMarkersMovedTest() {
            init(55);
            assertEquals(1, game.getPlayer(nick2).getPersonalBoard().getFaithTrack().getFaithMarkerPosition());
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            init(56);
            try {
                selectMarblesAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        } //todo: in tempMarblesContainSelectedMarbles method by default a red marble is removed from player's temp marble
        //todo: control for marbles don't exceed tempMarbles doesn't work
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
