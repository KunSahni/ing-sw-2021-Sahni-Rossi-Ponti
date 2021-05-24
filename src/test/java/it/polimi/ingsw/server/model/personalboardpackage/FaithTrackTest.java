package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class FaithTrackTest {
    FaithTrack faithTrack;

    @BeforeEach
    void setUp() throws IOException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        Game game = new Game(null,1, nicknames);
        faithTrack = game.getPlayer("Mario").getPersonalBoard().getFaithTrack();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    @DisplayName("Test moveMarker and getPosition")
    void moveMarkerAndGetPositionTest(int expectedPosition) {
        IntStream.range(0, expectedPosition).forEach(
                $ -> faithTrack.moveMarker()
        );
        int actualPosition = faithTrack.getFaithMarkerPosition();
        assertEquals(expectedPosition, actualPosition, "Error: was expecting " + expectedPosition + ", but received " + actualPosition);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8, 16, 24})
    @DisplayName("Test getVictoryPoints")
    void getVictoryPointsTest(int steps) {
        IntStream.range(0, steps).forEach(
                $ -> faithTrack.moveMarker()
        );
        int actualVictoryPoints = faithTrack.getVictoryPoints();
        int expectedVictoryPoints = switch (steps) {
            default -> 0;
            case 8 -> 4;
            case 16 -> 14;
            case 24 -> 29;
        };
        assertEquals(expectedVictoryPoints, actualVictoryPoints, "Error: was expecting " + expectedVictoryPoints + ", but received " + actualVictoryPoints);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("Test flipPopesFavor")
    void flipPopesFavorTest(int index) {
        IntStream.range(0, index*8).forEach(
                $ -> faithTrack.moveMarker()
        );
        faithTrack.flipPopesFavor(index);
        List<FavorStatus> popesFavors = faithTrack.getPopesFavors();
        assertEquals(FavorStatus.ACTIVE, popesFavors.get(index-1), "Error: the PersonalBoard did not flip the PopesFavor");
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 16, 24})
    @DisplayName("checkVaticanReport method test")
    void checkVaticanReportTest(int position){
        assertFalse(faithTrack.checkVaticanReport(position), "Error: vatican report results true even though no vatican reports were started so far");
        IntStream.range(0, position).forEach(
                $-> faithTrack.moveMarker()
        );
        assertTrue(faithTrack.checkVaticanReport(position), "Error: vatican report results false even though is currently on an inactive vatican report cell");
    }

    @Nested
    @DisplayName("getPopesFavors method tests")
    class GetPopesFavorsTests {
        List<FavorStatus> popesFavors;

        @BeforeEach
        void init() {
            popesFavors = faithTrack.getPopesFavors();
        }

        @Test
        @DisplayName("getPopesFavors safety test")
        void getPopesFavorsSafetyTest() {
            assertNotSame(faithTrack.getPopesFavors(), faithTrack.getPopesFavors());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(popesFavors);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(faithTrack.getPopesFavors(), faithTrack.getPopesFavors());
        }

        @Test
        @DisplayName("Returns correctly sized list")
        void sizeTest() {
            assertEquals(faithTrack.getPopesFavors().size(), 3);
        }
    }

}