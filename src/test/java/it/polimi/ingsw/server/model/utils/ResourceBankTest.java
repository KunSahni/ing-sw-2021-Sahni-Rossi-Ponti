package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.server.model.market.MarketMarble;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResourceBank tests")
public class ResourceBankTest {
    @Nested
    @DisplayName("getResourceFromMarble method tests")
    class GetResourcesFromMarbleTests {

        @Test
        @DisplayName("Returned value not null")
        void notNullTest() {
            assertNotNull(ResourceBank.getResourcesFromMarbles(new HashMap<>(){{put(MarketMarble.BLUE, 1);}}));
        }

        @Nested
        @DisplayName("Resources tests")
        class ResourcesTests {
            Map<MarketMarble, Integer> marbleMap;
            Map<Resource, Integer> resourceMap;

            @BeforeEach
            void init() {
                marbleMap = new HashMap<>() {{
                    put(MarketMarble.BLUE, 2);
                    put(MarketMarble.YELLOW, 1);
                }};
                resourceMap = ResourceBank.getResourcesFromMarbles(marbleMap);
            }

            @Test
            @DisplayName("Number of marbles passed and resources returned match")
            void matchingTotalQuantityTest() {
                int resourcesReturnedCount = resourceMap
                        .values()
                        .stream()
                        .reduce(0, Integer::sum);
                assertEquals(3, resourcesReturnedCount);
            }

            @Test
            @DisplayName("Number of unique marble and resource types match")
            void matchingResourceTypesCountTest() {
                assertEquals(marbleMap.keySet().size(), resourceMap.keySet().size());
            }

            @Test
            @DisplayName("Marble:Resource type matches are correct")
            void matchingResourceTypesTest() {
                assertAll(
                        () -> assertNotNull(resourceMap.get(Resource.SHIELD)),
                        () -> assertNotNull(resourceMap.get(Resource.COIN))
                );
            }

            @Test
            @DisplayName("Marble:Resource quantity matches are correct")
            void matchingEachResourceAmountTest() {
                assertAll(
                        () -> assertEquals(marbleMap.get(MarketMarble.BLUE),
                                resourceMap.get(Resource.SHIELD)),
                        () -> assertEquals(marbleMap.get(MarketMarble.YELLOW),
                                resourceMap.get(Resource.COIN))
                );
            }
        }
    }

    @Nested
    @DisplayName("getResources method tests")
    class GetResourcesTests {
        Map<Resource, Integer> resourceMap;
        Map<Resource, Integer> returnedMap;

        @BeforeEach
        void init() {
            resourceMap = new HashMap<>() {{
                put(Resource.SHIELD, 2);
                put(Resource.COIN, 3);
                put(Resource.SERVANT, 1);
            }};
            returnedMap = ResourceBank.getResources(resourceMap);
        }

        @Test
        @DisplayName("Returned value is not null")
        void notNullTest() {
            assertNotNull(returnedMap);
        }

        @Test
        @DisplayName("New map created on every call")
        void safetyTest() {
            assertNotSame(ResourceBank.getResources(resourceMap),
                    ResourceBank.getResources(resourceMap));
        }

        @Test
        @DisplayName("Correct Resource map created")
        void correctOutputTest() {
            assertEquals(resourceMap, returnedMap);
        }
    }
}