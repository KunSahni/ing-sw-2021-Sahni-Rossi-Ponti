package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartNextTurnActionTest {
    StartNextTurnAction startNextTurnAction;
    Game game;
    String nick1;
    String nick2;
    List<String> nicknameList;
    Server server;

    @BeforeEach
    void init(){
        deleteDir(new File("src/main/resources/games/1"));
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
        startNextTurnAction = new StartNextTurnAction(game);
    }

    @Test
    void executeTest() {
        game.connect(nick1);
        game.connect(nick2);
        game.getPlayer(nick1).startTurn();
        startNextTurnAction.execute();
        assertEquals(nick2, game.getCurrentTurnPlayer().getNickname());
    }

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
