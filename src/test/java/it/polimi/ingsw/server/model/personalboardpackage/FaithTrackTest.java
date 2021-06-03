package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class FaithTrackTest {
    FaithTrack faithTrack;
    ChangesHandler changesHandler;
    Game game;

    @BeforeEach
    void setUp() throws IOException {
        changesHandler = new ChangesHandler(1);
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        game = new Game(new Server(),1, nicknames);
        faithTrack = game.getPlayer("Mario").getPersonalBoard().getFaithTrack();
    }

    @AfterEach
    void tearDown() {
        changesHandler.publishGameOutcome(game);
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
        IntStream.range(1, steps+1).forEach(
                i -> {
                    faithTrack.moveMarker();
                    if(i%8==0)
                        faithTrack.flipPopesFavor(i/8);
                }
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
    @ValueSource(ints = {0, 1, 2, 3})
    @DisplayName("Test flipPopesFavor")
    void flipPopesFavorTest(int index) {
        List<FavorStatus> popesFavors = faithTrack.getPopesFavors();
        assertEquals(FavorStatus.INACTIVE, popesFavors.get(index==0? 1: index-1), "Error: faith track flipped or discarded PopesFavor which shouldn't be discarded");

        IntStream.range(0, index*8).forEach(
                $ -> faithTrack.moveMarker()
        );

        faithTrack.flipPopesFavor(index==0? 1: index);
        popesFavors = faithTrack.getPopesFavors();

        if(index!=0)
            assertEquals(FavorStatus.ACTIVE, popesFavors.get(index-1), "Error: faith track did not flip PopesFavor");
        else
            assertEquals(FavorStatus.DISCARDED, popesFavors.get(0), "Error: faith track flipped a PopesFavor which shouldn't be flipped");
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 8, 16, 24})
    @DisplayName("checkVaticanReport method test")
    void checkVaticanReportTest(int position){
        if(position !=5)
            assertTrue(faithTrack.checkVaticanReport(position), "Error: vatican report results true even though no vatican reports were started so far");
        IntStream.range(1, position+1).forEach(
                i -> {
                    faithTrack.moveMarker();
                    if(i%8==0)
                        faithTrack.flipPopesFavor(i/8);
                }
        );
        assertFalse(faithTrack.checkVaticanReport(position), "Error: vatican report results false even though is currently on an inactive vatican report cell");
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