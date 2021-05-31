package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.personalboard.SinglePlayerFaithTrack;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class LorenzoActionTest {
    LorenzoAction lorenzoAction;
    Game game;
    String nick1;
    List<String> nicknameList;
    Server server;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp(){
        changesHandler = new ChangesHandler(1);
        nick1 = "qwe";
        nicknameList = List.of(nick1);
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
        lorenzoAction = new LorenzoAction(game);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }

    @Nested
    @DisplayName("Tests for execute method")
    class executeTest{
        @Test
        @DisplayName("Black cross has been moved by two")
        void moveByTwoTest() {
            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.MOVE_BY_TWO)){
                game.getActionTokenDeck().pop();
            }
            lorenzoAction.execute();
            SinglePlayerFaithTrack singlePlayerFaithTrack = (SinglePlayerFaithTrack) game.getPlayer(nick1).getPersonalBoard().getFaithTrack();
            assertEquals(2, singlePlayerFaithTrack.getBlackCrossPosition());
        }

        @Test
        @DisplayName("Black cross have been moved by one and deck has been reset")
        void moveAndShuffleTest() {
            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.MOVE_AND_SHUFFLE)){
                game.getActionTokenDeck().pop();
            }

            SinglePlayerFaithTrack singlePlayerFaithTrack = (SinglePlayerFaithTrack) game.getPlayer(nick1).getPersonalBoard().getFaithTrack();
            lorenzoAction.execute();

            assertAll(
                    () -> assertEquals(1, singlePlayerFaithTrack.getBlackCrossPosition()),
                    () -> assertEquals(7, game.getActionTokenDeck().getCurrentDeck().size())
            );
        }

        @Test
        @DisplayName("Two blue development cards have been discarded")
        void removeBlueTest() {
            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.REMOVE_BLUE)){
                game.getActionTokenDeck().pop();
            }

            lorenzoAction.execute();

            assertEquals(2, game.getDevelopmentCardsBoard().peekBoard()[2][1].getDeck().size());
        }

        @Test
        @DisplayName("Two green development cards have been discarded")
        void removeGreenTest() {
            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.REMOVE_GREEN)){
                game.getActionTokenDeck().pop();
            }

            lorenzoAction.execute();

            assertEquals(2, game.getDevelopmentCardsBoard().peekBoard()[2][0].getDeck().size());
        }

        @Test
        @DisplayName("Two yellow development cards have been discarded")
        void removeYellowTest() {
            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.REMOVE_YELLOW)){
                game.getActionTokenDeck().pop();
            }

            lorenzoAction.execute();

            assertEquals(2, game.getDevelopmentCardsBoard().peekBoard()[2][2].getDeck().size());
        }

        @Test
        @DisplayName("Two purple development cards have been discarded")
        void removePurpleTest() {
            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.REMOVE_PURPLE)){
                game.getActionTokenDeck().pop();
            }

            lorenzoAction.execute();

            assertEquals(2, game.getDevelopmentCardsBoard().peekBoard()[2][3].getDeck().size());
        }

        @Test
        @DisplayName("No cards of a certain color remain so the game ends")
        void onlyOneCardRemain() {
            while (game.getDevelopmentCardsBoard().peekBoard()[0][0].getDeck().size()!=2){
                game.getDevelopmentCardsBoard().discardTwo(Color.GREEN);
            }
            game.getDevelopmentCardsBoard().pick(Level.LEVEL3, Color.GREEN);
            lorenzoAction.execute();
            assertFalse(server.getPlayers().containsValue(1));
        }

        @Test
        @DisplayName("Black cross reached the end of the track and game ends")
        void blackCrossReachEndOfFaithTrack() {
            SinglePlayerFaithTrack singlePlayerFaithTrack = (SinglePlayerFaithTrack) game.getPlayer(nick1).getPersonalBoard().getFaithTrack();

            while (singlePlayerFaithTrack.getBlackCrossPosition() != 23){
                singlePlayerFaithTrack.moveBlackCross();
            }

            while (!game.getActionTokenDeck().getCurrentDeck().get(game.getActionTokenDeck().getCurrentDeck().size()-1).equals(ActionToken.MOVE_BY_TWO)){
                game.getActionTokenDeck().pop();
            }
            lorenzoAction.execute();

            assertFalse(server.getPlayers().containsValue(1));
        }
    }
}
