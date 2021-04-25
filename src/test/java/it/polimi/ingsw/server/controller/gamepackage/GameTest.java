package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Game game;

    @BeforeEach
    void intit(){
        game = new Game(1, 2);
    }

    @Test
    @DisplayName("Game has been created correctly")
    void createGameTest(){
        Player player = new Player("Nick", game);
        assertEquals(1, game.getGameID());
    }

    @Test
    @DisplayName("All players have been added")
    void addPlayerTest(){
        int expected = 4;
        game = new Game(1, 4);
        Player nick = new Player("Nick", game);
        Player tom = new Player("Tom", game);
        Player paperino = new Player("Paperino", game);
        Player topolino = new Player("Topolino", game);
        assertAll(
                ()-> assertEquals(expected, game.getPlayers().size()),
                ()-> assertTrue(game.getPlayers().get(0).getNickname().equals("Nick")),
                ()-> assertTrue(game.getPlayers().get(1).getNickname().equals("Tom")),
                ()-> assertTrue(game.getPlayers().get(2).getNickname().equals("Paperino")),
                ()-> assertTrue(game.getPlayers().get(3).getNickname().equals("Topolino"))
        );
    }

    @Test
    @DisplayName("Current player turn pass to next player")
    void nextPlayerTurnTest(){
        String expectedString = "Tom";
        Player player = new Player("Nick", game);
        Player player1 = new Player("Tom", game);
        game.nextTurn();
        assertEquals(expectedString, game.getPlayers().get(0).getNickname());
    }

    @Test
    void nextSinglePlayerTurnTest(){
        String expectedString = "Nick";

        game = new Game(1,1);
        Player player = new Player("Nick", game);
        game.nextSinglePlayerTurn();
        assertEquals(expectedString, game.getPlayers().get(0).getNickname());
    }

    @Test
    @DisplayName("Players who are qualified flip their Pope's favor")
    void flipOtherPopesFavorTest(){
        Player player = new Player("Nick", game);
        Player litto = new Player("Litto", game);
        game.getPlayers().get(1).getPersonalBoard().getFaithTrack().moveMarker(5);
        game.startVaticanReport(1);
        List<FavorStatus> playerLittoFavorStatusList= new ArrayList<>();
        for (FavorStatus favorStatus: game.getPlayers().get(1).getPersonalBoard().getFaithTrack().getPopesFavors()) {
            if(favorStatus.equals(FavorStatus.ACTIVE)) playerLittoFavorStatusList.add(favorStatus);
        }
        List<FavorStatus> playerNickFavorStatusList= new ArrayList<>();
        for (FavorStatus favorStatus: game.getPlayers().get(0).getPersonalBoard().getFaithTrack().getPopesFavors()) {
            if(favorStatus.equals(FavorStatus.ACTIVE)) playerNickFavorStatusList.add(favorStatus);
        }
        assertAll(
                ()-> assertEquals(1, playerLittoFavorStatusList.size()),
                ()-> assertEquals(0, playerNickFavorStatusList.size())
        );
    }

    @Test
    void runTest(){

    }

    @DisplayName("Players have been sorted correctly")
    @Test
    void playersSorted() {
        game = new Game(1, 4);
        Player nick = new Player("Nick", game);
        Player tom = new Player("Tom", game);
        Player paperino = new Player("Paperino", game);
        Player topolino = new Player("Topolino", game);

        game.getPlayers().get(0).setPosition(3);
        game.getPlayers().get(1).setPosition(4);
        game.getPlayers().get(2).setPosition(1);
        game.getPlayers().get(3).setPosition(2);

        Player player3 = game.getPlayers().get(0);
        Player player4 = game.getPlayers().get(1);
        Player player1 = game.getPlayers().get(2);
        Player player2 = game.getPlayers().get(3);

        game.sortPlayers();

        assertAll(
                ()->assertEquals(player1.getNickname(), game.getPlayers().get(0).getNickname()),
                ()->assertEquals(player2.getNickname(), game.getPlayers().get(1).getNickname()),
                ()->assertEquals(player3.getNickname(), game.getPlayers().get(2).getNickname()),
                ()->assertEquals(player4.getNickname(), game.getPlayers().get(3).getNickname())
        );
    }

    @Nested
    class moveOtherMarkerTest{
        @Test
        @DisplayName("In multiplayer game move other marker correctly")
        void moveOtherMarkerMultiplayerTest() {
            Player nick = new Player("Nick", game);
            Player lit = new Player("Lit", game);
            int expected = game.getPlayers().get(1).getPersonalBoard().getFaithTrack().getFaithMarkerPosition()+1;
            game.moveOtherMarkers(game.getPlayers().get(0));
            assertEquals(expected, game.getPlayers().get(1).getPersonalBoard().getFaithTrack().getFaithMarkerPosition());
        }

        @Test
        void moveOtherMarkerSinglePlayerTest() {
            Game game = new Game(1, 1);
            Player nick = new Player("Nick", game);

            int expected = ((SinglePlayerFaithTrack) game.getPlayers().get(0).getPersonalBoard().getFaithTrack()).getBlackCrossPosition()+1;
            game.moveOtherMarkers(game.getPlayers().get(0));
            assertEquals(expected, ((SinglePlayerFaithTrack) game.getPlayers().get(0).getPersonalBoard().getFaithTrack()).getBlackCrossPosition());
        }
    }
}
