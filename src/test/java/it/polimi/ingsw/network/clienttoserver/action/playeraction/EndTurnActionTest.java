package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class EndTurnActionTest {
    EndTurnAction endTurnAction;
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
        game.getPlayer(nick1).addAction(ExecutedActions.TURN_ENDED_ACTION);
        endTurnAction = new EndTurnAction();
        endTurnAction.setNickname(nick1);
        endTurnAction.setGame(game);
        game.setState(GameState.IN_GAME);
    }

    @Nested
    @DisplayName("Tests for execute method")
    class executeTests {
        @Test
        @DisplayName("A player has reached the end of the faithTrack and Last Round has been triggered")
        void playerReachedEndOfFaithTrackTest() {
            for (int i=0; i<=23; i++) {
                game.getPlayer(nick1).getPersonalBoard().getFaithTrack().moveMarker(); //todo: a player can move aside from faithTrack 24 position
            }
            endTurnAction.execute();
            assertEquals(GameState.LAST_ROUND, game.getCurrentState());
        }

        @Test
        @DisplayName("A player has bought 7 development cards so last round has been triggered")
        void playerBought7DevelopmentCardsTest() {
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 1);
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL2, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 1);
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL3, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 1);
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 2);
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL2, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 2);
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL3, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 2);
            game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 3);

            endTurnAction.execute();
            assertEquals(GameState.LAST_ROUND, game.getCurrentState());
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
            try {
                endTurnAction.runChecks();
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
                endTurnAction.runChecks();
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
            game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);

            try {
                endTurnAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot end your turn at this time")){
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
