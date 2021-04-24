package it.polimi.ingsw.server.model.actiontoken;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests ActionTokenDeck behavior, all tests refer to getFullDeck for
 * checks.
 */
@DisplayName("ActionTokenDeck tests: getFullDeck is assumed to work properly")
public class ActionTokenDeckTest {
    ActionTokenDeck actionTokenDeck;

    @BeforeEach
    void init() {
        actionTokenDeck = new ActionTokenDeck();
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
            assertNotSame(ActionTokenDeck.getFullDeck(), ActionTokenDeck.getFullDeck());
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
            assertEquals(ActionTokenDeck.getFullDeck().size(), currentDeck.size());
        }

        @Test
        @DisplayName("All elements match the actual deck")
        void allElementsMatchTest() {
            assertTrue(currentDeck.containsAll(ActionTokenDeck.getFullDeck()));
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
