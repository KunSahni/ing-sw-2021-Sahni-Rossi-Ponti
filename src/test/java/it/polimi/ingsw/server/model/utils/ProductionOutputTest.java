package it.polimi.ingsw.server.model.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionOutputTest {
    ProductionOutput testProductionOutput;
    Map<Resource, Integer> testResourcesMap;

    @BeforeEach
    void init() {
        testResourcesMap = new HashMap<>();
        Random random = new Random();
        List<Resource> resourcesList = Arrays.asList(Resource.values());
        Collections.shuffle(resourcesList);
        testResourcesMap.put(Resource.COIN, 2);
        testResourcesMap.put(Resource.SHIELD, 1);
        testProductionOutput = new ProductionOutput(2, testResourcesMap);
    }

    /**
     * Safety test for getResources method
     */
    @Test
    void getResourcesSafetyTest() {
        assertNotSame(testProductionOutput.getResources(), testProductionOutput.getResources());
    }

    @Test
    @DisplayName("Attributes correctness test")
    void getterTests(){
        assertAll(
                () -> assertEquals(2, testProductionOutput.getFaithIncrement(), "Error: slot returned wrong faith increment"),
                () -> assertEquals(testResourcesMap, testProductionOutput.getResources(), "Error: slot returned wrong resources")
        );
    }
}
