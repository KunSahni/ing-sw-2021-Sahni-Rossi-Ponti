package it.polimi.ingsw.server.model.developmentcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DevelopmentCardsDeckTest {
    DevelopmentCardsDeck deck;

    /**
     * getDeck safety test
     */
    @Test
    @DisplayName("getDeck safety test")
    void getDeckSafetyTest() {
        assertNotSame(deck.getDeck(), deck.getDeck());
    }

    /**
     * Initializes the global deck to a BLUE LEVEL1 deck.
     */
    @BeforeEach
    void init() {
        deck = ((new DevelopmentCardsBoard()).peekBoard())[2][1];
    }

    /**
     * Pop must remove the topmost card and return it. Size and returned card get checked.
     */
    @Nested
    @DisplayName("pop tests")
    class popTests {
        List<DevelopmentCard> initialDeck;
        DevelopmentCard poppedCard;
        List<DevelopmentCard> finalDeck;

        @BeforeEach
        void init() {
            initialDeck = deck.getDeck();
            poppedCard = deck.pop();
            finalDeck = deck.getDeck();
        }

        @Test
        @DisplayName("Size of deck decreased by one")
        void sizeDecreaseByOne() {
            assertEquals(initialDeck.size() - 1, finalDeck.size());
        }

        @Test
        @DisplayName("Correct card popped")
        void correctCardPoppedTest() {
            DevelopmentCard expectedCard = initialDeck.get(initialDeck.size() - 1);
            assertEquals(expectedCard, poppedCard);
        }

        @Test
        @DisplayName("Rest of the deck remains unchanged")
        void restOfDeckUnchangedTest() {
            assertEquals(initialDeck.subList(0, initialDeck.size() - 1), finalDeck);
        }
    }

    /**
     * Peek method must return the topmost card, leaving the deck unchanged
     */
    @Nested
    @DisplayName("peek tests")
    class peekTests {
        List<DevelopmentCard> initialDeck;
        DevelopmentCard peekedCard;
        List<DevelopmentCard> finalDeck;

        @BeforeEach
        void init() {
            initialDeck = deck.getDeck();
            peekedCard = deck.peek();
            finalDeck = deck.getDeck();
        }

        @Test
        @DisplayName("Deck unchanged")
        void deckUnchangedTest() {
            assertEquals(initialDeck, finalDeck);
        }

        @Test
        @DisplayName("Consequent calls return the same card")
        void sameReturnTest() {
            assertEquals(deck.peek(), deck.peek());
        }

        @Test
        @DisplayName("Correct card peeked")
        void correctCardPeekedTest() {
            DevelopmentCard expectedCard = initialDeck.get(initialDeck.size() - 1);
            assertEquals(expectedCard, peekedCard);
        }
    }
}
