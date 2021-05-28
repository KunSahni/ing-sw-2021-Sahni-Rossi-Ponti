package it.polimi.ingsw.server.model.market;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Market class behavior, getMarketMarble is used as platform for integrity checks
 */
public class MarketTest {
    private Market market;
    Game game;

    /**
     * Returns a List of all MarketMarbles from the market.
     * Marbles are sorted so the first four correspond to the
     * row that is closest to becoming an ExtraMarble.
     * @param market a Market object.
     * @return the List of marbles.
     */
    static List<MarketMarble> marketToMarbleList(Market market) {
        return Arrays.stream(market.getMarblesLayout())
                .flatMap(Arrays::stream)
                .collect(toList());
    }

    @BeforeEach
    void init() throws IOException {
        game = new Game(new Server(), 1, new ArrayList<>());
        market = game.getMarket();
    }

    @AfterEach
    void tearDown() {
        new ChangesHandler(1).publishGameOutcome(game);
    }

    @Nested
    @DisplayName("getMarblesLayout tests")
    class GetMarblesLayoutTests {
        // TODO: does not check deep enough
        @Test
        @DisplayName("Safety test")
        void getMarblesLayoutSafetyTest() {
            assertNotSame(market.getMarblesLayout(), market.getMarblesLayout());
        }

        @Test
        @DisplayName("Returns a non-null value")
        void notNullTest() {
            assertNotNull(market.getMarblesLayout());
        }

        @Test
        @DisplayName("All slots are non-null")
        void allSlotsAreMarblesTest() {
            assertTrue(marketToMarbleList(market).stream().allMatch(Objects::nonNull));
        }

        @Test
        @DisplayName("Market is composed of 12 elements")
        void twelveElementsTest() {
            assertEquals(12, marketToMarbleList(market).size());
        }

        @RepeatedTest(value = 3, name = "Row {currentRepetition}")
        @DisplayName("Returned rows are made of 4 elements")
        void fourElementsRowTest(RepetitionInfo repetitionInfo) {
            assertEquals(4, market.getMarblesLayout()[repetitionInfo.getCurrentRepetition() - 1].length);
        }

        @Test
        @DisplayName("Subsequent method calls return the same layout")
        void sameReturnTests() {
            assertEquals(marketToMarbleList(market), marketToMarbleList(market));
        }
    }

    @Nested
    @DisplayName("Other tests: meaningful only if all getMarblesLayout tests are successful")
    class OtherTests {
        @Nested
        @DisplayName("chooseRow method tests")
        class ChooseRowTests {
            @Test
            @DisplayName("Returns a non-null Object")
            void nonNullTest() {
                assertNotNull(market.chooseRow(2));
            }

            @Test
            @DisplayName("Returns a non-empty map")
            void notEmptyMap() {
                assertTrue(market.chooseRow(2).size() > 0);
            }

            @Test
            @DisplayName("Map contains four elements")
            void fourMarbleMap() {
                assertEquals(4, market.chooseRow(2)
                        .values()
                        .stream()
                        .reduce(0, Integer::sum)
                );
            }

            @DisplayName("Returns the correct MarketMarbles")
            @RepeatedTest(value = 3, name = "Row {currentRepetition} chosen")
            void correctMarbles(RepetitionInfo info) {
                List<MarketMarble> expectedMarbles =
                        Arrays.asList(market.getMarblesLayout()[info.getCurrentRepetition() - 1]);
                // On each repetition a row is retrieved via chooseRow
                // Each map entry gets checked to verify the value is
                // the same of the expected value.
                assertTrue(market.chooseRow(info.getCurrentRepetition() - 1)
                        .entrySet()
                        .stream()
                        .allMatch(entry ->
                                entry.getValue() == expectedMarbles.stream()
                                        .filter(marble -> marble == entry.getKey())
                                        .count()
                        )
                );
            }

            @DisplayName("Row re-arrangement is correct")
            @RepeatedTest(value = 3, name = "Row {currentRepetition} chosen")
            void rightArrangementTest(RepetitionInfo info) {
                // Create an Expected list containing the Marbles
                // from the row without the marble at the start,
                // which will become the ExtraMarble on the chooseRow
                // call.
                List<MarketMarble> expectedMarbles = new ArrayList<>(
                        Arrays.asList(
                                market.getMarblesLayout()[info.getCurrentRepetition() - 1])
                                .subList(1, 4)
                );
                // Add the current ExtraMarble to the list to
                // account for its insertion on chooseRow call.
                expectedMarbles.add(market.getExtraMarble());
                market.chooseRow(info.getCurrentRepetition() - 1);
                // Check if the new row matches the prediction.
                assertEquals(expectedMarbles, Arrays.asList(
                        market.getMarblesLayout()[info.getCurrentRepetition() - 1])
                );
            }

            @DisplayName("Other rows unchanged")
            @RepeatedTest(value = 3, name = "Row {currentRepetition} chosen")
            void otherRowsUnchangedTest(RepetitionInfo info) {
                // Get a list containing all MarketMarbles in the
                // Market except the row that will be chosen.
                List<MarketMarble> expectedMarbles = marketToMarbleList(market);
                expectedMarbles.subList(
                        4 * info.getCurrentRepetition() - 4,
                        4 * info.getCurrentRepetition()
                ).clear();
                market.chooseRow(info.getCurrentRepetition() - 1);
                // Get the updated MarketMarbles list, still
                // excluding the modified row.
                List<MarketMarble> actualMarbles = marketToMarbleList(market);
                actualMarbles.subList(
                        4 * info.getCurrentRepetition() - 4,
                        4 * info.getCurrentRepetition()
                ).clear();
                assertEquals(expectedMarbles, actualMarbles);
            }
        }

        @Nested
        @DisplayName("chooseColumn method tests")
        class ChooseColumnTests {
            @Test
            @DisplayName("Returns a non-null Object")
            void nonNullTest() {
                assertNotNull(market.chooseColumn(2));
            }

            @Test
            @DisplayName("Returns a non-empty map")
            void notEmptyMap() {
                assertTrue(market.chooseColumn(2).size() > 0);
            }

            @Test
            @DisplayName("Map contains three elements")
            void threeMarbleMap() {
                assertEquals(3, market.chooseColumn(2)
                        .values()
                        .stream()
                        .reduce(0, Integer::sum)
                );
            }

            @DisplayName("Returns the correct MarketMarbles")
            @RepeatedTest(value = 4, name = "Column {currentRepetition} chosen")
            void correctMarbles(RepetitionInfo info) {
                List<MarketMarble> expectedMarbles = IntStream.range(0, 3)
                        .mapToObj(i -> market.getMarblesLayout()[i][info.getCurrentRepetition() - 1])
                        .collect(toList());
                // On each repetition a column is retrieved via chooseColumn
                // Each value of the returned map is compared for equality
                // to the count of marbles of that value's key.
                assertTrue(market.chooseColumn(info.getCurrentRepetition() - 1)
                        .entrySet()
                        .stream()
                        .allMatch(entry ->
                                entry.getValue() == expectedMarbles.stream()
                                        .filter(marble -> marble == entry.getKey())
                                        .count()
                        )
                );
            }

            @DisplayName("Column re-arrangement is correct")
            @RepeatedTest(value = 4, name = "Column {currentRepetition} chosen")
            void rightArrangementTest(RepetitionInfo info) {
                // Create an Expected list containing the Marbles
                // from the column without the marble in position 0,
                // which will become the ExtraMarble on the chooseRow
                // call.
                List<MarketMarble> expectedMarbles = IntStream.range(1, 3)
                        .mapToObj(i -> market.getMarblesLayout()[i][info.getCurrentRepetition() - 1])
                        .collect(Collectors.toList());
                // Add the current ExtraMarble to the list to
                // account for its insertion on chooseRow call.
                expectedMarbles.add(market.getExtraMarble());
                market.chooseColumn(info.getCurrentRepetition() - 1);
                // Check if the new column matches the prediction.
                assertEquals(expectedMarbles, IntStream.range(0, 3)
                        .mapToObj(i -> market.getMarblesLayout()[i][info.getCurrentRepetition() - 1])
                        .collect(Collectors.toList())
                );
            }

            @DisplayName("Other columns unchanged")
            @RepeatedTest(value = 4, name = "Column {currentRepetition} chosen")
            void otherRowsUnchangedTest(RepetitionInfo info) {
                // Get a list containing all MarketMarbles in the
                // Market except the column that will be chosen.
                List<MarketMarble> expectedMarbles = marketToMarbleList(market);
                IntStream.range(0, 3).forEach(i -> expectedMarbles.remove(
                        (2 - i) * 4 + info.getCurrentRepetition() - 1
                ));
                market.chooseColumn(info.getCurrentRepetition() - 1);
                // Get the updated MarketMarbles list, still
                // excluding the modified column.
                List<MarketMarble> actualMarbles = marketToMarbleList(market);
                IntStream.range(0, 3).forEach(i -> actualMarbles.remove(
                        (2 - i) * 4 + info.getCurrentRepetition() - 1
                ));
                assertEquals(expectedMarbles, actualMarbles);
            }
        }

        @Nested
        @DisplayName("getExtraMarble method tests")
        class GetExtraMarbleTests {
            @Test
            @DisplayName("Returns non-null object")
            void notNullTest() {
                assertNotNull(market.getExtraMarble());
            }

            @DisplayName("Correct update on chooseRow")
            @RepeatedTest(value = 3, name = "Row {currentRepetition} chosen")
            void chooseRowTest(RepetitionInfo info) {
                MarketMarble expected = market.getMarblesLayout()[info.getCurrentRepetition() - 1][0];
                market.chooseRow(info.getCurrentRepetition() - 1);
                assertEquals(expected, market.getExtraMarble());
            }

            @DisplayName("Correct update on chooseColumn")
            @RepeatedTest(value = 4, name = "Column {currentRepetition} chosen")
            void chooseColumnTest(RepetitionInfo info) {
                MarketMarble expected = market.getMarblesLayout()[0][info.getCurrentRepetition() - 1];
                market.chooseColumn(info.getCurrentRepetition() - 1);
                assertEquals(expected, market.getExtraMarble());
            }
        }
    }
}
