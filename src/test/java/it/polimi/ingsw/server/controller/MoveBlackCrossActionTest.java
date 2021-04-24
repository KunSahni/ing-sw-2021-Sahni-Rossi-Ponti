package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.message.action.MoveBlackCrossAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveBlackCrossActionTest {
    MoveBlackCrossAction moveBlackCrossAction;
    Player player;

    @BeforeEach
    void init() {
        Game game = new Game(1, 1);
        player = new Player("Nick", game);
        Turn turn = new Turn(game, player);
        moveBlackCrossAction = new MoveBlackCrossAction(turn);
    }

    @Test
    @DisplayName("Black Cross moved forward two tiles")
    void twoTileBlackCrossMovementTest() {
        int initialBlackCrossPosition =
                ((SinglePlayerFaithTrack) player.getPersonalBoard().getFaithTrack())
                        .getBlackCrossPosition();
        moveBlackCrossAction.forward();
        int finalBlackCrossPosition =
                ((SinglePlayerFaithTrack) player.getPersonalBoard().getFaithTrack())
                        .getBlackCrossPosition();
        assertEquals(initialBlackCrossPosition + 2, finalBlackCrossPosition);
    }
}
