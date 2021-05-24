package it.polimi.ingsw.server.model.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {
    ResourceManager resourceManager;
    Map<Resource, Integer> newResources;

    @BeforeEach
    void setUp() {
        resourceManager = new ResourceManager();
        newResources = new HashMap<>();
        newResources.put(Resource.COIN, 1);
        newResources.put(Resource.SERVANT, 2);
        resourceManager.storeResources(newResources);
    }

    @Test
    @DisplayName("Test storeResources and getResources")
    void storeResourcesAndGetStoredResourcesTest() {
        Map<Resource, Integer> storedResources = resourceManager.getStoredResources();
        assertEquals(newResources, storedResources, "Error: resource manager did not store or save properly resources");
    }

    @Test
    @DisplayName("Test contains")
    void containsTest() {
        Map<Resource, Integer> newResources = new HashMap<>();
        newResources.put(Resource.COIN, 1);
        newResources.put(Resource.SERVANT, 2);
        resourceManager.storeResources(newResources);

        Map<Resource, Integer> request = new HashMap<>();
        newResources.put(Resource.COIN, 1);

        assertTrue(resourceManager.contains(request), "Error: the resource manager returned false even though it contains the requested resource");
    }

    @Test
    @DisplayName("Test discardResources")
    void discardResourcesTest() {
        Map<Resource, Integer> request = new HashMap<>();
        request.put(Resource.COIN, 1);

        resourceManager.discardResources(request);

        newResources.remove(Resource.COIN);
        Map<Resource, Integer> storedResources = resourceManager.getStoredResources();
        assertEquals(newResources, storedResources, "Error: resource manager did not properly discard resources");
    }

    @Test
    @DisplayName("Test getResourceCount")
    void getResourceCountTest() {
        int expectedCount = newResources.values().stream().mapToInt(
                i -> i
        ).sum();
        int actualCount = resourceManager.getResourceCount();

        assertEquals(expectedCount, actualCount, "Error: resource manager should have a resource count of " + expectedCount + ", but instead received " + actualCount);
    }

    @Nested
    @DisplayName("getStoredResources method tests")
    class GetStoredResourcesTests {
        Map<Resource, Integer> storedResources;

        @BeforeEach
        void init() {
            storedResources = resourceManager.getStoredResources();
        }

        @Test
        @DisplayName("getLeaderCards safety test")
        void getLeaderCardsSafetyTest() {
            assertNotSame(resourceManager.getStoredResources(), resourceManager.getStoredResources());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(storedResources);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(resourceManager.getStoredResources(), resourceManager.getStoredResources());
        }

        @Test
        @DisplayName("Returns correctly sized list")
        void sizeTest() {
            assertTrue(resourceManager.getStoredResources().size()<=4);
        }
    }
}