package it.polimi.ingsw.server.model.leadercard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardsDeckTest {
    LeaderCardsDeck deck;

    @BeforeEach
    void init() {
        deck = new LeaderCardsDeck();
    }

    @Test
    @DisplayName("getDeck safety test")
    void getDeckSafetyTest() {
        assertNotSame(deck.getDeck(), deck.getDeck());
    }

    @Nested
    @DisplayName("popFour tests")
    class popFourTests {
        List<LeaderCard> initialDeck;
        List<LeaderCard> fourCards;
        List<LeaderCard> finalDeck;

        @BeforeEach
        void init() {
            initialDeck = deck.getDeck();
            fourCards = deck.popFour();
            finalDeck = deck.getDeck();
        }

        @Test
        @DisplayName("Four cards are drawn")
        void drawnFourTest() {
            assertEquals(4, fourCards.size());
        }

        @Test
        @DisplayName("The correct cards have been returned")
        void correctCardsDrawnTest() {
            List<LeaderCard> trailingFourCards = initialDeck.subList(initialDeck.size() - 4, initialDeck.size());
            assertTrue(trailingFourCards.containsAll(fourCards));
        }

        @Test
        @DisplayName("The returned cards got removed from the deck")
        void removedFromDeckTest() {
            assertFalse(finalDeck.containsAll(fourCards));
        }

        @Test
        @DisplayName("Deck's size decreased by 4")
        void decreasedByFourTest() {
            assertEquals(initialDeck.size() - 4, finalDeck.size());
        }

        @Test
        @DisplayName("Rest of the deck unchanged")
        void restOfDeckUnchanged() {
            assertEquals(initialDeck.subList(0, initialDeck.size() - 4), finalDeck);
        }
    }
}
