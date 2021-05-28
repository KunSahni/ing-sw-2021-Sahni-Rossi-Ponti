package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test StoreLeaderCard behavior.
 * All tests assume that the getResourceCount method works properly.
 */
@DisplayName("StoreLeaderCard tests")
public class StoreLeaderCardTest {
    StoreLeaderCard leaderCard;
    Game game;

    /**
     * Utility method to encapsulate creation of shield hashmaps.
     * @param amount number of shields that will be contained
     */
    Map<Resource, Integer> createShieldsMap(int amount) {
        return new HashMap<>() {{
            put(Resource.SHIELD, amount);
        }};
    }

    /**
     * This method returns a LeaderCard with a specified LeaderCardAbility
     * @param leaderCardAbility the ability that the returned LeaderCard should have
     * @return a LeaderCard of the specified LeaderCardAbility
     */
    private LeaderCard getLeaderCardWithAbility (LeaderCardAbility leaderCardAbility) throws FileNotFoundException {
        LeaderCardsDeck leaderCardsDeck = game.getLeaderCardsDeck();
        leaderCardsDeck.shuffle();
        Optional<LeaderCard> leaderCard = leaderCardsDeck.popFour().stream().filter(
                leaderCard1 -> leaderCard1.getAbility().equals(leaderCardAbility)
        ).findFirst();
        return leaderCard.orElseGet(() -> {
            try {
                return getLeaderCardWithAbility(leaderCardAbility);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @BeforeEach
    void init() throws IOException {
        game = new Game(new Server(), 1, new ArrayList<>());
        leaderCard = (StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE);
    } //todo:i costruttori delle LC non dovrebbero essere privati?

    @AfterEach
    void tearDown() {
        new ChangesHandler(1).publishGameOutcome(game);
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
