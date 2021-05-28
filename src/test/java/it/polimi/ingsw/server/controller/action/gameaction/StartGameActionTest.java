package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartGameActionTest {
    StartGameAction startGameAction;
    Game game;
    String nick1;
    String nick2;
    List<String> nicknameList;
    Server server;

    @BeforeEach
    void init(){
        deleteDir();
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        nicknameList = List.of(nick1, nick2);
        try {
            game = new Game(server, 4, nicknameList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        startGameAction = new StartGameAction(game);
    }

    @Test
    void executeTest() {
        startGameAction.execute();
        assertEquals(GameState.IN_GAME, game.getCurrentState());
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
}
