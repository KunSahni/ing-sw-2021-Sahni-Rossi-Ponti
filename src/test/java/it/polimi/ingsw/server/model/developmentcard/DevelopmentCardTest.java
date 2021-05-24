package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardTest {
    DevelopmentCard card;

    @BeforeEach
    void init() throws FileNotFoundException {
        card = new ChangesHandler(1).readDevelopmentCardsBoard().peekBoard()[2][1].peek();
    }

    @Nested
    @DisplayName("Safety tests")
    class SafetyTests {
        @Test
        @DisplayName("getInputResources safety test")
        void getInputResourcesSafetyTest() {
            assertNotSame(card.getInputResources(), card.getInputResources());
        }

        @Test
        @DisplayName("getOutputResources safety test")
        void getOutputResourcesSafetyTest() {
            assertNotSame(card.getOutputResources(), card.getOutputResources());
        }

        @Test
        @DisplayName("getCost safety test")
        void getCostSafetyTest() {
            assertNotSame(card.getCost(), card.getCost());
        }
    }

    /**
     * Generated ProductionOutput must match card details
     */
    @Test
    @DisplayName("produce's ProductionOutput matches card details")
    void produceTest() {
        ProductionOutput productionOutput = card.produce();
        assertAll(
                () -> assertEquals(card.getOutputResources(), productionOutput.getResources()),
                () -> assertEquals(card.getFaithIncrement(), productionOutput.getFaithIncrement())
        );
    }
}
