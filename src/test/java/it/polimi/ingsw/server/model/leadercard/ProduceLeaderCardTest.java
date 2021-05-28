package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProduceLeaderCardTest {
    ProduceLeaderCard testCard;
    Game game;

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
        testCard = (ProduceLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.PRODUCE);
    } //todo:i costruttori delle LC non dovrebbero essere privati?

    @AfterEach
    void tearDown() {
        new ChangesHandler(1).publishGameOutcome(game);
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
