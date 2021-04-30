package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class ResetAndMoveActionTest {
    ResetAndMoveAction resetAndMoveAction;
    Game game;
    PersonalBoard personalBoard;

    @BeforeEach
    void init() {
        game = new Game(1, 1);
        Player player = new Player("Nick", game);
        Turn turn = new Turn(game, player);
        personalBoard = player.getPersonalBoard();
        resetAndMoveAction = new ResetAndMoveAction(turn);
    }

    @Test
    @DisplayName("Action Token Deck gets reset")
    void actionTokenDeckResetTest() {
        IntStream.range(0, 4).forEach(i -> game.getActionTokenDeck().pop());
        resetAndMoveAction.forward();
        assertEquals(7, game.getActionTokenDeck().getCurrentDeck().size());
    }

    @Test
    @DisplayName("Black cross moves by one on the Faith Track")
    void blackCrossOneStepTest() {
        int initialPosition = ((SinglePlayerFaithTrack) personalBoard.getFaithTrack())
                .getBlackCrossPosition();
        resetAndMoveAction.forward();
        assertEquals(initialPosition + 1,
                ((SinglePlayerFaithTrack) personalBoard.getFaithTrack())
                .getBlackCrossPosition());
    }
}
