package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class DealLeaderCardsActionTest {
    DealLeaderCardsAction dealLeaderCardsAction;
    Game game;
    String nick1;
    String nick2;
    String nick3;
    String nick4;
    List<String> nicknameList;
    Server server;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp(){
        changesHandler = new ChangesHandler(1);
        nick1 = "qwe";
        nick2 = "asd";
        nick3 = "zxc";
        nick4 = "poi";
        nicknameList = List.of(nick1, nick2, nick3, nick4);
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
                ()-> assertEquals(4, game.getPlayer(nick2).getTempLeaderCards().size()),
                ()-> assertEquals(4, game.getPlayer(nick3).getTempLeaderCards().size()),
                ()-> assertEquals(4, game.getPlayer(nick4).getTempLeaderCards().size())
        );
    }

    @Test
    @DisplayName("All assigned leader cards are different")
    void differentCardsTest(){
        List <LeaderCard> leaderCards = new ArrayList<>();
        for (Player player: game.getPlayerList()) {
            for (LeaderCard leaderCard: player.getTempLeaderCards()) {
                if (leaderCards.contains(leaderCard)){
                    fail();
                }
                else{
                    leaderCards.add(leaderCard);
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
