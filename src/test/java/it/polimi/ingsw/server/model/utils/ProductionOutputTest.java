package it.polimi.ingsw.server.model.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ProductionOutputTest {
    ProductionOutput testProductionOutput;

    @BeforeEach
    void init() {
        Map<Resource, Integer> testResourcesMap = new HashMap<>();
        Random random = new Random();
        List<Resource> resourcesList = Arrays.asList(Resource.values());
        Collections.shuffle(resourcesList);
        for (int i=random.nextInt(resourcesList.size()+1); i<resourcesList.size(); i++) {
            testResourcesMap.put(resourcesList.get(i), 1+random.nextInt(9));
        }
        testProductionOutput = new ProductionOutput(random.nextInt(3),
                testResourcesMap.keySet().size()==0
                        ? null
                        : testResourcesMap);
    }
    /**
     * Safety test for getResources method
     */
    @Test
    void getResourcesSafetyTest() {
        assertNotSame(testProductionOutput.getResources(), testProductionOutput.getResources());
    }
}
