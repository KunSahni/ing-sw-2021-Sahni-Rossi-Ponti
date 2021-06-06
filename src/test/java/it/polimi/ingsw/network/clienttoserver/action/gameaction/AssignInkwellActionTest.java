package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class AssignInkwellActionTest {
    AssignInkwellAction assignInkwellAction;
    Game game;
    String nick1;
    String nick2;
    String nick3;
    String nick4;
    List<String> nickList;
    Server server;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp() {
        changesHandler = new ChangesHandler(1);
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
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }

    @Nested
    @DisplayName("Players positions are set correctly")
    class executeTest{
        @Test
        @DisplayName("Players position are in the right range")
        void rightRangePositions()
        {
            assignInkwellAction.execute();

            assertAll(
                    () -> assertTrue(game.getPlayer(nick1).getPosition() > 0 && game.getPlayer(nick1).getPosition() <= 4),
                    () -> assertTrue(game.getPlayer(nick2).getPosition() > 0 && game.getPlayer(nick2).getPosition() <= 4),
                    () -> assertTrue(game.getPlayer(nick3).getPosition() > 0 && game.getPlayer(nick3).getPosition() <= 4),
                    () -> assertTrue(game.getPlayer(nick4).getPosition() > 0 && game.getPlayer(nick4).getPosition() <= 4)
            );
        }

        @Test
        @DisplayName("Players position are different")
        void noSamePositionTest(){
            assignInkwellAction.execute();
            int positionNick1 = game.getPlayer(nick1).getPosition();
            int positionNick2 = game.getPlayer(nick2).getPosition();
            int positionNick3 = game.getPlayer(nick3).getPosition();
            int positionNick4 = game.getPlayer(nick4).getPosition();
            assertAll(
                    () -> assertTrue(positionNick1 != positionNick2 && positionNick1 != positionNick3 && positionNick1 != positionNick4),
                    () -> assertTrue(positionNick2 != positionNick3 && positionNick2 != positionNick4),
                    () -> assertTrue(positionNick3 != positionNick4 )
            );
        }

        @Test
        @DisplayName("Faith track markers of players in position 3 and 4 have been moved")
        void playerMovedTest() {
            assignInkwellAction.execute();
            Player player1 = game.getPlayerList().get(2);
            Player player2 = game.getPlayerList().get(3);
            assertAll(
                    () -> assertEquals(1, player1.getPersonalBoard().getFaithTrack().getFaithMarkerPosition()),
                    () -> assertEquals(1, player2.getPersonalBoard().getFaithTrack().getFaithMarkerPosition())
            );
        }
    }
}
