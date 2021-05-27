package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DealLeaderCardsActionTest {
    DealLeaderCardsAction dealLeaderCardsAction;
    Game game;
    String nick1;
    String nick2;
    List<String> nicknameList;
    Server server;

    @BeforeEach
    void init(){
        deleteDir(new File("src/main/resources/games/1"));
        nick1 = "qwe";
        nick2 = "asd";
        nicknameList = List.of(nick1, nick2);
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            game = new Game(server, 1, nicknameList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dealLeaderCardsAction = new DealLeaderCardsAction(game);
    }

    @Test
    @DisplayName("TempLeaderCards have been assigned correctly")
    void executeTest() {
        dealLeaderCardsAction.execute();
        assertAll(
                ()-> assertEquals(4, game.getPlayer(nick1).getTempLeaderCards().size()),
                ()-> assertEquals(4, game.getPlayer(nick2).getTempLeaderCards().size())
        );
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
