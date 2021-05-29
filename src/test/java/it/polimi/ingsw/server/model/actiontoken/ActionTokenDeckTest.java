package it.polimi.ingsw.server.model.actiontoken;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests ActionTokenDeck behavior, all tests refer to getFullDeck for
 * checks.
 */
@DisplayName("ActionTokenDeck tests: getFullDeck is assumed to work properly")
public class ActionTokenDeckTest {
    ActionTokenDeck actionTokenDeck;
    ChangesHandler changesHandler;
    Game game;

    @BeforeEach
    void init() throws IOException {
        changesHandler = new ChangesHandler(1);
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        game = new Game(new Server(),1, nicknames);
        actionTokenDeck = changesHandler.readActionTokenDeck();
    }

    @AfterEach
    void tearDown() {
        changesHandler.publishGameOutcome(game);
    }

    @Nested
    @DisplayName("Safety Tests: mutable Objects should not be returned by reference")
    class SafetyTests {
        @Test
        @DisplayName("getCurrentDeck safety test")
        void getCurrentDeckSafetyTest() {
            assertNotSame(actionTokenDeck.getCurrentDeck(), actionTokenDeck.getCurrentDeck());
        }

        @Test
        @DisplayName("getFullDeck safety test")
        void getFullDeckSafetyTest() {
            assertNotSame(actionTokenDeck.getFullDeck(), actionTokenDeck.getFullDeck());
        }
    }

    @Nested
    @DisplayName("getCurrentDeck method tests")
    class GetCurrentDeckTests {
        List<ActionToken> currentDeck;

        @BeforeEach
        void init() {
            currentDeck = actionTokenDeck.getCurrentDeck();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(currentDeck);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(actionTokenDeck.getCurrentDeck(), actionTokenDeck.getCurrentDeck());
        }

        @Test
        @DisplayName("Returns correctly sized deck")
        void fullSizedDeckTest() {
            assertEquals(actionTokenDeck.getFullDeck().size(), currentDeck.size());
        }

        @Test
        @DisplayName("All elements match the actual deck")
        void allElementsMatchTest() {
            assertTrue(currentDeck.containsAll(actionTokenDeck.getFullDeck()));
        }
    }

    @Nested
    @DisplayName("pop method tests")
    class PopTests {
        ActionToken poppedToken;
        List<ActionToken> initialDeck;

        @BeforeEach
        void init() {
            initialDeck = actionTokenDeck.getCurrentDeck();
            poppedToken = actionTokenDeck.pop();
        }

        @Test
        @DisplayName("Popped token not null")
        void notNullTest() {
            assertNotNull(poppedToken);
        }

        @Test
        @DisplayName("Reduces deck by one")
        void reducedByOneTest() {
            assertEquals(initialDeck.size() - 1, actionTokenDeck.getCurrentDeck().size());
        }

        @Test
        @DisplayName("Returns the correct ActionToken")
        void correctTokenPoppedTest() {
            assertEquals(initialDeck.get(initialDeck.size() - 1), poppedToken);
        }

        @Test
        @DisplayName("Rest of the deck is unchanged")
        void restOfDeckUnchangedTest() {
            assertEquals(initialDeck.subList(0, initialDeck.size() - 1),
                    actionTokenDeck.getCurrentDeck());
        }
    }
}
