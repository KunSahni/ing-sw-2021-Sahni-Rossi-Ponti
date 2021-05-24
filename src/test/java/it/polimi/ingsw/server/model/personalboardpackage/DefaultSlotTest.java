package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.model.personalboard.DefaultSlot;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSlotTest {

    @ParameterizedTest
    @EnumSource(Resource.class)
    @DisplayName("Test produce")
    void produceTest(Resource resource) {
        //Create a Map of resources
        Map<Resource, Integer> expectedOutput = new HashMap<>();
        expectedOutput.put(resource, 1);

        //Produce from DefaultSlot with the Map created earlier
        ProductionOutput productionOutput = DefaultSlot.produce(expectedOutput);

        //Extract the Map received from DefaultSlot
        Map<Resource, Integer> actualOutput = productionOutput.getResources();

        //Check if the two maps are the same
        assertEquals(expectedOutput, actualOutput, "Error: was expecting " + expectedOutput + ", but received " + actualOutput);
    }
}