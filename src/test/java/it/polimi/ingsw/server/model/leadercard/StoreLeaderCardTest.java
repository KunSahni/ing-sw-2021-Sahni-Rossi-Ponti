package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test StoreLeaderCard behavior.
 * All tests assume that the getResourceCount method works properly.
 */
@DisplayName("StoreLeaderCard tests")
public class StoreLeaderCardTest {
    StoreLeaderCard leaderCard;

    /**
     * Utility method to encapsulate creation of shield hashmaps.
     * @param amount number of shields that will be contained
     */
    Map<Resource, Integer> createShieldsMap(int amount) {
        return new HashMap<>() {{
            put(Resource.SHIELD, amount);
        }};
    }

    @BeforeEach
    void init() {
        leaderCard = new StoreLeaderCard(1,
                new LeaderCardRequirements(null, null),
                Resource.SHIELD, null);
    }

    @DisplayName("storeResources tests")
    @ParameterizedTest(name = "Store {0} resource/s")
    @ValueSource(ints = {1, 2})
    void storeResourcesTest(int amount) {
        int initialQuantity = leaderCard.getResourceCount();
        leaderCard.storeResources(createShieldsMap(amount));
        assertAll(
                () -> assertEquals(0, initialQuantity),
                () -> assertEquals(amount, leaderCard.getResourceCount())
        );
    }

    @DisplayName("discardResources tests")
    @ParameterizedTest(name = "Discard {1} out of {0} resources contained")
    @CsvSource({"1, 1", "2, 2", "2, 1"})
    void discardResourcesTest(int startingQuantity, int discardQuantity) {
        leaderCard.storeResources(createShieldsMap(startingQuantity));
        leaderCard.discardResources(createShieldsMap(discardQuantity));
        assertEquals(startingQuantity - discardQuantity, leaderCard.getResourceCount());
    }

    @Test
    @DisplayName("convertToDumb method test")
    void convertToDumbTest(){
        DumbStoreLeaderCard dumbStoreLeaderCard = (DumbStoreLeaderCard) leaderCard.convertToDumb();
        assertAll(
                ()-> assertEquals(leaderCard.getStoredType(), dumbStoreLeaderCard.getStoredType(),"Error: dumb leader card contains a different stored resource type than the one in the real leader card"),
                ()-> assertEquals(leaderCard.getResourceCount(), dumbStoreLeaderCard.getResourceCount(),"Error: dumb leader card has a different resource count than the real leader card"),
                ()-> assertEquals(leaderCard.getStoredResources(), dumbStoreLeaderCard.getStoredResources(),"Error: dumb leader card contains different resources than the one in the real leader card")
        );
    }

}
