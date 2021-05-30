package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardsDeckTest {
    LeaderCardsDeck deck;
    ChangesHandler changesHandler;
    Game game;

    @BeforeEach
    void init() throws IOException {
        game = new Game(new Server(), 1, new ArrayList<>());
        changesHandler = new ChangesHandler(1);
        deck = game.getLeaderCardsDeck();
    }

    @AfterEach
    void tearDown() {
        changesHandler.publishGameOutcome(game);
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
            List<LeaderCard> trailingFourCards = initialDeck.subList(0, 4);
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
            assertEquals(initialDeck.subList(4, initialDeck.size()), finalDeck);
        }
    }
}
