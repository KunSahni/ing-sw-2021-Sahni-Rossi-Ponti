package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;
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

        @Test
        @DisplayName("getAbility() method test")
        void getAbilityMethodTest() {
            assertEquals(LeaderCardAbility.PRODUCE, testCard.getAbility());
        }
    }

    @Test
    @DisplayName("equals method test")
    void equalsTest(){
        ProduceLeaderCard leaderCard2 = new ProduceLeaderCard(1,
                new LeaderCardRequirements(null, null),
                Resource.COIN, 1);
        assertEquals(testCard, leaderCard2, "Error: equals method returned false on two identical objects");
    }

    @Test
    @DisplayName("getInputResource method test")
    void getInputResourceTest(){
        assertEquals(testCard.getInputResource(), Resource.COIN, "Error: leader card contains a different input resource than the one passed in the constructor");
    }

    @Test
    @DisplayName("convertToDumb method test")
    void convertToDumbTest(){
        DumbProduceLeaderCard dumbProduceLeaderCard = (DumbProduceLeaderCard) testCard.convertToDumb();
        assertAll(
                ()-> assertEquals(testCard.getInputResource(), dumbProduceLeaderCard.getInputResource(),"Error: dumb leader card contains a different input resource than the one in the real leader card"),
                ()-> assertEquals(testCard.getAbility(), dumbProduceLeaderCard.getAbility(),"Error: dumb leader card has a different ability than the real leader card"),
                ()-> assertEquals(testCard.getFaithIncrement(), dumbProduceLeaderCard.getFaithIncrement(),"Error: dumb leader card contains a different faith increment than the one in the real leader card"),
                ()-> assertEquals(testCard.getVictoryPoints(), dumbProduceLeaderCard.getVictoryPoints(),"Error: dumb leader card has different victory points than the real leader card"),
                ()-> assertEquals(testCard.getLeaderCardRequirements(), dumbProduceLeaderCard.getLeaderCardRequirements(),"Error: dumb leader card has different requirements than the real leader card")
                );
    }
}
