package it.polimi.ingsw.server.model.developmentcard;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import it.polimi.ingsw.server.model.ChangesHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.FileNotFoundException;
import java.util.*;

public class DevelopmentCardsBoardTest {
    DevelopmentCardsBoard board;
    ChangesHandler changesHandler;

    @BeforeEach
    void init() throws FileNotFoundException {
        changesHandler = new ChangesHandler(1);
        board = changesHandler.readDevelopmentCardsBoard();
    }

    /**
     * Returns a List of all DevelopmentCardsDeck from the given board.
     * Decks are sorted in descending level order.
     * @param board a DevelopmentCardsBoard object
     * @return the List of cards
     */
    static List<DevelopmentCardsDeck> boardToDeckList(DevelopmentCardsBoard board) {
        return Arrays.stream(board.peekBoard())
                .flatMap(Arrays::stream)
                .collect(toList());
    }

    /**
     * Returns a List of all DevelopmentCards from the given board.
     * Cards are sorted in descending level order, the original order
     * of the stacks which made up all the DevelopmentCardsDecks has
     * been inverted. The last cards in the list are the ones that get
     * popped first from the stacks.
     * @param board a DevelopmentCardsBoard object
     * @return the List of cards
     */
    static List<DevelopmentCard> boardToCardList(DevelopmentCardsBoard board) {
        return boardToDeckList(board).stream()
                .flatMap(deck -> deck.getDeck().stream())
                .collect(toList());
    }

    @Nested
    @DisplayName("Safety tests")
    class safetyTests {
        /**
         * Safety test for peekBoard
         */
        @Test
        @DisplayName("peekBoard() returns a copy of the board")
        void peekBoardSafetyTest() {
            assertNotSame(board.peekBoard(), board.peekBoard());
        }
    }

    @Nested
    @DisplayName("peekBoard tests")
    class peekBoardTests {
        @Test
        @DisplayName("Not null")
        void notNullTest() {
            assertNotNull(board.peekBoard());
        }

        @Nested
        @DisplayName("Integrity tests on peeked board")
        class integrityTests {

            /**
             * Disables all logical tests in case the peeked board is null
             */
            @BeforeEach
            void checkTestability() {
                assumeTrue(Objects.nonNull(board.peekBoard()));
            }

            @Test
            @DisplayName("All DevelopmentCardsDeck are not null")
            void notNullDecksTest() {
                assertTrue(boardToDeckList(board).stream().allMatch(Objects::nonNull));
            }

            @Test
            @DisplayName("Consequent calls return the same board")
            void sameBoardReturnedTest() {
                assertEquals(boardToCardList(board), boardToCardList(board));
            }

            @Test
            @DisplayName("At least one DevelopmentCardsDeck contains cards")
            void notEmptyDecksTest() {
                assertTrue(boardToDeckList(board).stream().anyMatch(deck -> deck.getDeck().size() > 0));
            }

            @Test
            @DisplayName("All cards in each DevelopmentCardsDeck have the same level")
            void sameLevelTest() {
                assertTrue(boardToDeckList(board).stream().allMatch(
                        deck -> deck.getDeck().stream().allMatch(
                                card -> card.getLevel() == deck.peek().getLevel())
                        )
                );
            }

            @Test
            @DisplayName("All cards in each DevelopmentCardsDeck have the same color")
            void sameColorTest() {
                assertTrue(boardToDeckList(board).stream().allMatch(
                        deck -> deck.getDeck().stream().allMatch(
                                card -> card.getColor() == deck.peek().getColor())
                        )
                );
            }

            @Test
            @DisplayName("All decks have different Color-Level combinations")
            void allDifferentColorLevelTest() {
                assertTrue(
                        boardToDeckList(board).stream().allMatch(
                                analyzedDeck -> 1 == boardToDeckList(board).stream().filter(
                                        deck -> deck.peek().getLevel() == analyzedDeck.peek().getLevel() &&
                                                deck.peek().getColor() == analyzedDeck.peek().getColor())
                                        .count()
                        )
                );
            }
        }
    }

    @Nested
    @DisplayName("Other tests: only meaningful if all peekBoard tests work")
    class otherTests {
        /**
         * Having to rely on peekBoard() to analyze the Peek method behavior,
         * checking for non-nullity gives a small safety net.
         */
        @BeforeEach
        void checkTestability() {
            assumeTrue(Objects.nonNull(board.peekBoard()));
        }

        @Nested
        @DisplayName("pick tests")
        class pickCardTests {
            @Test
            @DisplayName("Picked card is not null")
            void notNullTest() {
                assertNotNull(board.pick(Level.LEVEL1, Color.PURPLE));
            }

            @Test
            @DisplayName("Reduced by one total number of cards on the board")
            void reducedBoardTest() {
                long initialNum = boardToCardList(board).size();
                board.pick(Level.LEVEL1, Color.PURPLE);
                long finalNum = boardToCardList(board).size();
                assertEquals(initialNum-1, finalNum);
            }

            @Test
            @DisplayName("Picked card present in board before picking")
            void containedInInitialStreamTest() {
                List<DevelopmentCard> initialDevCardList = boardToCardList(board);
                DevelopmentCard pickedCard = board.pick(Level.LEVEL1, Color.BLUE);
                assertTrue(initialDevCardList.contains(pickedCard));
            }

            @Test
            @DisplayName("Pick card not present in board after picking")
            void notContainedInFinalStreamTest() {
                DevelopmentCard pickedCard = board.pick(Level.LEVEL1, Color.BLUE);
                List<DevelopmentCard> finalDevCardList = boardToCardList(board);
                assertFalse(finalDevCardList.contains(pickedCard));
            }

            @Test
            @DisplayName("All un-picked decks have not changed size")
            void unchangedDecksSizeTest() {
                List<Integer> initialSizes = boardToDeckList(board).stream()
                        .filter(deck -> deck.peek().getLevel() != Level.LEVEL1 &&
                                deck.peek().getColor() != Color.BLUE)
                        .map(deck -> deck.getDeck().size())
                        .collect(toList());
                board.pick(Level.LEVEL1, Color.BLUE);
                List<Integer> finalSizes = boardToDeckList(board).stream()
                        .filter(deck -> deck.peek().getLevel() != Level.LEVEL1 &&
                                deck.peek().getColor() != Color.BLUE)
                        .map(deck -> deck.getDeck().size())
                        .collect(toList());
                assertEquals(initialSizes, finalSizes);
            }

            @Test
            @DisplayName("Picked deck decreased in size by 1")
            void pickedDeckSizeTest() {
                int initialDeckSize = boardToDeckList(board).stream()
                        .filter(deck -> deck.peek().getLevel() == Level.LEVEL1 &&
                                deck.peek().getColor() == Color.BLUE)
                        .mapToInt(deck -> deck.getDeck().size())
                        .sum();
                board.pick(Level.LEVEL1, Color.BLUE);
                int finalDeckSize = boardToDeckList(board).stream()
                        .filter(deck -> deck.peek().getLevel() == Level.LEVEL1 &&
                                deck.peek().getColor() == Color.BLUE)
                        .mapToInt(deck -> deck.getDeck().size())
                        .sum();
                assertEquals(initialDeckSize-1, finalDeckSize);
            }
        }

        @Nested
        @DisplayName("peekCard tests")
        class peekCardTests {
            @Test
            @DisplayName("Peeked card not null")
            void notNullTest() {
                assertNotNull(board.peekCard(Level.LEVEL1, Color.BLUE));
            }

            @Test
            @DisplayName("Board unchanged by peek method")
            void unchangedOnPeekTest() {
                List<DevelopmentCardsDeck> initialBoard = boardToDeckList(board);
                board.peekCard(Level.LEVEL1, Color.BLUE);
                List<DevelopmentCardsDeck> finalBoard = boardToDeckList(board);
                assertEquals(initialBoard, finalBoard);
            }

            @Test
            @DisplayName("Peek returned the correct card")
            void correctCardPeekedTest() {
                Optional<DevelopmentCard> expectedCard = boardToDeckList(board).stream()
                        .filter(deck -> deck.peek().getColor() == Color.BLUE &&
                                deck.peek().getLevel() == Level.LEVEL1)
                        .map(DevelopmentCardsDeck::peek)
                        .findFirst();
                assumeTrue(expectedCard.isPresent(), "Unexpected error: board is missing BLUE LEVEL1 deck");
                assertEquals(expectedCard.get(), board.peekCard(Level.LEVEL1, Color.BLUE));
            }
        }

        @Nested
        @DisplayName("discardTwo tests")
        class discardTwoTests {
            /**
             * Set the board status by specifying the sizes of BLUE cards decks
             * @param lv1DeckSize number of level 1 cards
             * @param lv2DeckSize number of level 2 cards
             * @param lv3DeckSize number of level 3 cards
             */
            void setStartingBoardArrangement(int lv1DeckSize, int lv2DeckSize, int lv3DeckSize) {
                for (int i=0; i<4-lv1DeckSize; i++) {
                    board.pick(Level.LEVEL1, Color.BLUE);
                }
                for (int i=0; i<4-lv2DeckSize; i++) {
                    board.pick(Level.LEVEL2, Color.BLUE);
                }
                for (int i=0; i<4-lv3DeckSize; i++) {
                    board.pick(Level.LEVEL3, Color.BLUE);
                }
            }

            /**
             * Returns the n bottom-most BLUE DevelopmentCards in the board.
             * @param cardsList List of cards that makes up the board
             * @param number quantity of cards to be returned
             * @return ordered list of DevelopmentCards. The last card in the list is
             * the bottom-most card among ones of the same color.
             */
            List<DevelopmentCard> getLastCards(List<DevelopmentCard> cardsList, int number) {
                List<DevelopmentCard> allCardsOfColor = cardsList.stream()
                        .filter(card -> card.getColor() == Color.BLUE)
                        .collect(toList());
                return new ArrayList<>(allCardsOfColor.subList(allCardsOfColor.size() - number, allCardsOfColor.size()));
            }

            @DisplayName("Tests with different deck arrangements")
            @ParameterizedTest(name = "{0} lv1, {1} lv2, {2} lv3: {3}-card discard expected")
            @CsvFileSource(files = "src/test/resources/discard-two-deck-arrangement.csv")
            void discardTwoCardsTest(int lv1DeckSize, int lv2DeckSize, int lv3DeckSize, int numOfDiscardsExpected) {
                setStartingBoardArrangement(lv1DeckSize, lv2DeckSize, lv3DeckSize);
                List<DevelopmentCard> expectedDiscardedCards =
                        getLastCards(boardToCardList(board), numOfDiscardsExpected);
                List<DevelopmentCard> initialBoard = boardToCardList(board);
                board.discardTwo(Color.BLUE);
                List<DevelopmentCard> finalBoard = boardToCardList(board);
                assertAll(
                        () -> assertTrue(initialBoard.containsAll(expectedDiscardedCards)),
                        () -> assertEquals(initialBoard.size()-numOfDiscardsExpected, finalBoard.size())
                );
                if (expectedDiscardedCards.size() == 0) {
                    assertTrue(finalBoard.containsAll(expectedDiscardedCards));
                } else {
                    assertFalse(finalBoard.containsAll(expectedDiscardedCards));
                }
            }
        }
    }
}