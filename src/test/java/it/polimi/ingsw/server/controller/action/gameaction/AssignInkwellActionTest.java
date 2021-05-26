package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssignInkwellActionTest {
    AssignInkwellAction assignInkwellAction;
    Game game;
    String nick1;
    String nick2;
    String nick3;
    String nick4;
    List<String> nickList;
    Server server;

    @Test
    @DisplayName("Players positions are set correctly")
    void executeTest(){
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        nick3 = "zxc";
        nick4 = "poi";
        nickList = List.of(nick1, nick2, nick3, nick4);
        try {
            game = new Game(server,1, nickList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assignInkwellAction = new AssignInkwellAction(game);
        assignInkwellAction.execute();

        for (String nick: nickList) {
            assertTrue(game.getPlayer(nick).getPosition()>0 && game.getPlayer(nick).getPosition()<=4);
        }
    }
}
