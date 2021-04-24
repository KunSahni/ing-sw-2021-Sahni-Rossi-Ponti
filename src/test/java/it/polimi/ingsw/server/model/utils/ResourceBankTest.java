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
            assertNotNull(ResourceBank.getResourceFromMarble(new HashMap<>(){{put(MarketMarble.BLUE, 1);}}));
        }

        @Nested
        @DisplayName("Faith increment tests")
        class FaithIncrementTests {
            @Test
            @DisplayName("Only one red marble supplied")
            void onlySingleFaithIncrementTest() {
                Map<MarketMarble, Integer> marbleMap = new HashMap<>() {{
                    put(MarketMarble.RED, 1);
                }};
                ProductionOutput productionOutput = ResourceBank.getResourceFromMarble(marbleMap);
                assertAll(
                        () -> assertNull(productionOutput.getResources()),
                        () -> assertEquals(1, productionOutput.getFaithIncrement())
                );
            }

            @Test
            @DisplayName("No red marbles supplied")
            void noRedMarbles() {
                Map<MarketMarble, Integer> marbleMap = new HashMap<>() {{
                    put(MarketMarble.BLUE, 1);
                }};
                ProductionOutput productionOutput = ResourceBank.getResourceFromMarble(marbleMap);
                assertEquals(0, productionOutput.getFaithIncrement());
            }
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
                resourceMap = ResourceBank.getResourceFromMarble(marbleMap).getResources();
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
    @DisplayName("getResource method tests")
    class GetResourceTests {
        Resource paramResource;
        Map<Resource, Integer> returnedMap;

        @BeforeEach
        void init() {
            paramResource = Resource.SHIELD;
            returnedMap = ResourceBank.getResource(paramResource);
        }

        @Test
        @DisplayName("Returned value not null")
        void notNullTest() {
            assertNotNull(returnedMap);
        }

        @Test
        @DisplayName("Returned map contains only one resource type")
        void singleResourceTypeTest() {
            assertEquals(1, returnedMap.keySet().size());
        }

        @Test
        @DisplayName("Returned map contains only one resource")
        void singleResourceQuantityTest() {
            assertEquals(1, returnedMap
                    .values()
                    .stream()
                    .reduce(0, Integer::sum)
            );
        }

        @Test
        @DisplayName("Given and returned resource types match")
        void matchingTypeTest() {
            assertTrue(returnedMap.containsKey(paramResource));
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