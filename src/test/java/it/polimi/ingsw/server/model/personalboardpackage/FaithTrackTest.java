package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FaithTrackTest {

    FaithTrack faithTrack;

    @BeforeEach
    void setUp() {
        Game game = new Game(1, 1);
        Player player = new Player("Mario",game);
        PersonalBoard board = new PersonalBoard(player);
        faithTrack = new FaithTrack(player);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    @DisplayName("test moveMarker and getPosition")
    void testMoveMarkerAndGetPosition(int expectedPosition) {
        faithTrack.moveMarker(expectedPosition);
        int actualPosition = faithTrack.getFaithMarkerPosition();
        assertEquals(expectedPosition, actualPosition, "Error: was expecting " + expectedPosition + ", but received " + actualPosition);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8, 16, 24})
    @DisplayName("test getVictoryPoints")
    void testGetVictoryPoints(int steps) {
        faithTrack.moveMarker(steps);
        int actualVictoryPoints = faithTrack.getVictoryPoints();
        int expectedVictoryPoints = switch (steps) {
            default -> 0;
            case 8 -> 4;
            case 16 -> 14;
            case 20 -> 29;
        };
        assertEquals(expectedVictoryPoints, actualVictoryPoints, "Error: was expecting " + expectedVictoryPoints + ", but received " + actualVictoryPoints);
    }

    //todo: reimplement using the right getter for popes favors
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("test flipPopesFavor")
    void testFlipPopesFavor(int index) {
        int victoryPointsBeforeFlip = faithTrack.getVictoryPoints();
        faithTrack.moveMarker(index*8);
        int victoryPointsAfterFlip = faithTrack.getVictoryPoints();
        int expectedValue = victoryPointsBeforeFlip + 3 + index;
        int actualValue = victoryPointsAfterFlip - victoryPointsBeforeFlip;
        assertEquals(expectedValue, actualValue, "Error: was expecting " + expectedValue + ", but received " + actualValue);
    }
}