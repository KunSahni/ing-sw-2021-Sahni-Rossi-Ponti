package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SinglePlayerFaithTrackTest {
    SinglePlayerFaithTrack singlePlayerFaithTrack;

    @BeforeEach
    void setUp() {
        Game game = new Game(1, 1);
        Player player = new Player("Mario",game);
        PersonalBoard board = new PersonalBoard(player);
        singlePlayerFaithTrack = new SinglePlayerFaithTrack(player);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Test moveBlackCross and getBlackCrossPosition")
    void moveBlackCrossAndGetBlackCrossPositionTest(int expectedPosition) {
        for(int i =0; i< expectedPosition; i++)
            singlePlayerFaithTrack.moveBlackCross();
        int actualPosition = singlePlayerFaithTrack.getBlackCrossPosition();
        assertEquals(expectedPosition, actualPosition, "Error: was expecting " + expectedPosition + ", but received " + actualPosition);
    }

}