package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.ActivateLeaderCardAction;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.remoteview.RemoteView;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    Controller controller;
    Game game;
    List<String> nicknameList;
    RemoteView remoteView;
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
        nicknameList = List.of(nick1, nick2);
        try {
            game = new Game(server, 1, nicknameList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = new Controller(game);
        remoteView = new RemoteView(controller);
        controller.setRemoteView(remoteView);
    }

    @Test
    @DisplayName("Player has been connected correctly")
    void connectPlayerTest() {
        controller.connectPlayer(nick1, new Connection(new Socket(), server));

        assertTrue(game.getPlayer(nick1).isConnected());
    }

    @Test
    @DisplayName("Player has been disconnected correctly")
    void disconnectPlayerTest() {
        controller.connectPlayer(nick1, new Connection(new Socket(), server));
        controller.disconnectPlayer(nick1);

        assertFalse(game.getPlayer(nick1).isConnected());
    }

    @Test
    @DisplayName("Current turn player disconnect, so the turn is passed to the other player")
    void actualTurnPlayerDisconnectTest() {
        controller.connectPlayer(nick1, new Connection(new Socket(), server));
        controller.connectPlayer(nick2, new Connection(new Socket(), server));
        game.getPlayer(nick1).startTurn();

        controller.disconnectPlayer(nick1);

        assertEquals(game.getPlayer(nick2), game.getCurrentTurnPlayer());
    }

    @Test
    @DisplayName("After Leader Cards have been picked inkwell is assigned")
    void pickedLeaderCardsTest() {
        game.setState(GameState.PICKED_LEADER_CARDS);

        new Controller(game);

        assertEquals(GameState.ASSIGNED_INKWELL, game.getCurrentState());
    }

    @Test
    @DisplayName("After resources have been picked game is started")
    void pickedResourcesTest() {
        game.setState(GameState.PICKED_RESOURCES);

        new Controller(game);

        assertEquals(GameState.IN_GAME, game.getCurrentState());
    }

    @Test
    @DisplayName("Turn pass to the next player")
    void inGameTest() {
        game.setState(GameState.IN_GAME);
        game.getPlayer(nick1).startTurn();
        game.getPlayer(nick1).addAction(ExecutedActions.TURN_ENDED_ACTION);
        controller.connectPlayer(nick1, new Connection(new Socket(), server));
        controller.connectPlayer(nick2, new Connection(new Socket(), server));
        new Controller(game);

        assertEquals(game.getPlayer(nick2), game.getCurrentTurnPlayer());
    }

    @Test
    @DisplayName("Turn is still of player1")
    void noTurnFinishedTest() {
        game.setState(GameState.IN_GAME);
        game.getPlayer(nick1).startTurn();
        game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
        controller.connectPlayer(nick1, new Connection(new Socket(), server));
        controller.connectPlayer(nick2, new Connection(new Socket(), server));
        new Controller(game);

        assertEquals(game.getPlayer(nick1), game.getCurrentTurnPlayer());
    }

    @Test
    void dealtLeaderCardsTest() {
        game.setState(GameState.DEALT_LEADER_CARDS);

        new Controller(game);

        assertEquals(GameState.DEALT_LEADER_CARDS, game.getCurrentState());
    }

//    @Test
//    @DisplayName("When onNext is invoked the action is performed correctly")
//    void onNextTest() {
//        DumbConvertLeaderCard dumbLeaderCard = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.STONE, 1)), Resource.COIN));
//        ActivateLeaderCardAction activateLeaderCardAction = new ActivateLeaderCardAction(dumbLeaderCard);
//        activateLeaderCardAction.setNickname(nick1);
//        game.getPlayer(nick1).setTempLeaderCards(List.of(dumbLeaderCard.convert()));
//        ConvertLeaderCard leaderCard = dumbLeaderCard.convert();
//        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(leaderCard));
//        game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
//        game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 1);
//        game.getPlayer(nick1).getPersonalBoard().storeInStrongbox(Map.of(Resource.STONE, 1));
//        game.getPlayer(nick1).startTurn();
//        controller.onNext(activateLeaderCardAction);
//
//        assertAll(
//                ()-> assertEquals(leaderCard, game.getPlayer(nick1).getPersonalBoard().getLeaderCards().get(0)),
//                ()-> assertTrue(game.getPlayer(nick1).getPersonalBoard().getLeaderCards().get(0).isActive())
//        );
//    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }
}
