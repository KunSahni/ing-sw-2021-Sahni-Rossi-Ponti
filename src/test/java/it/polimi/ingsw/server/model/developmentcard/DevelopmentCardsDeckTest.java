package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.server.model.ChangesHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DevelopmentCardsDeckTest {
    DevelopmentCardsDeck deck;

    @BeforeEach
    void init() throws IOException {
        ChangesHandler changesHandler = new ChangesHandler(1);
        changesHandler.createGameFilesFromBlueprint(new ArrayList<>());
        deck = changesHandler.readDevelopmentCardsBoard().peekBoard()[2][1];
    }

    @AfterEach
    void tearDown() {
        new ChangesHandler(1).publishGameOutcome(null);
    }

    /**
     * getDeck safety test
     */
    @Test
    @DisplayName("getDeck safety test")
    void getDeckSafetyTest() {
        assertNotSame(deck.getDeck(), deck.getDeck());
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
            DevelopmentCard expectedCard = initialDeck.get(0);
            assertEquals(expectedCard, poppedCard);
        }

        @Test
        @DisplayName("Rest of the deck remains unchanged")
        void restOfDeckUnchangedTest() {
            assertEquals(initialDeck.subList(1, initialDeck.size()), finalDeck);
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
            DevelopmentCard expectedCard = initialDeck.get(0);
            assertEquals(expectedCard, peekedCard);
        }
    }
}
