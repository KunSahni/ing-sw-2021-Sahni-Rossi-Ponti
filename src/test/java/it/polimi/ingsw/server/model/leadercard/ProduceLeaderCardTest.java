package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProduceLeaderCardTest {
    ProduceLeaderCard testCard;

    @BeforeEach
    void init() {
        testCard = new ProduceLeaderCard(1,
                new LeaderCardRequirements(null, null),
                Resource.COIN, 1);
    }

    /**
     * Execute test production to verify that a one element-one value map is created
     */
    @Nested
    @DisplayName("produce tests")
    class produceTests {
        ProductionOutput productionOutput;

        @BeforeEach
        void init() {
            productionOutput = testCard.produce(Resource.SHIELD);
        }

        @Test
        @DisplayName("Single faith increment produced")
        void singleFaithIncrementTest() {
            assertEquals(1, productionOutput.getFaithIncrement());
        }

        @Test
        @DisplayName("Single resource produced")
        void singleResourceTest() {
            assertEquals(1, productionOutput.getResources().get(Resource.SHIELD));
        }

        @Test
        @DisplayName("Single resource type produced")
        void singleResourceTypeTest() {
            assertEquals(1, productionOutput.getResources().size());
        }
    }
}
