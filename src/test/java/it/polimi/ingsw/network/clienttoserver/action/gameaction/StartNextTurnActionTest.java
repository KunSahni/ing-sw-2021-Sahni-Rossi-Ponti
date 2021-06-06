package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartNextTurnActionTest {
    StartNextTurnAction startNextTurnAction;
    Game game;
    String nick1;
    String nick2;
    String nick3;
    List<String> nicknameList;
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
        nick3 = "zxc";
        nicknameList = List.of(nick1, nick2, nick3);
        try {
            game = new Game(server, 1, nicknameList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        game.getPlayer(nick1).setPosition(1);
        game.getPlayer(nick2).setPosition(2);
        game.getPlayer(nick3).setPosition(3);
        startNextTurnAction = new StartNextTurnAction(game);
    }

    @Test
    @DisplayName("Action has been executed correctly")
    void executeTest() {
        game.connect(nick1);
        game.connect(nick3);
        game.getPlayer(nick1).startTurn();
        startNextTurnAction.execute();
        assertEquals(nick3, game.getCurrentTurnPlayer().getNickname());
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }
}
