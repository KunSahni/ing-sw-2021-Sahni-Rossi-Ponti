package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

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
    @DisplayName("Test moveMarker and getPosition")
    void moveMarkerAndGetPositionTest(int expectedPosition) {
        faithTrack.moveMarker(expectedPosition);
        int actualPosition = faithTrack.getFaithMarkerPosition();
        assertEquals(expectedPosition, actualPosition, "Error: was expecting " + expectedPosition + ", but received " + actualPosition);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 8, 16, 24})
    @DisplayName("Test getVictoryPoints")
    void getVictoryPointsTest(int steps) {
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

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("Test flipPopesFavor")
    void flipPopesFavorTest(int index) {
        faithTrack.moveMarker(index*8);
        faithTrack.flipPopesFavor(index);
        List<FavorStatus> popesFavors = faithTrack.getPopesFavors();
        assertEquals(FavorStatus.ACTIVE, popesFavors.get(index-1), "Error: the PersonalBoard did not flip the PopesFavor");
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