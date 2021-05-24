package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.personalboard.SinglePlayerFaithTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SinglePlayerFaithTrackTest {
    SinglePlayerFaithTrack singlePlayerFaithTrack;

    @BeforeEach
    void setUp() throws IOException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        Game game = new Game(null,1, nicknames);
        singlePlayerFaithTrack = (SinglePlayerFaithTrack) game.getPlayer("Mario").getPersonalBoard().getFaithTrack();
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