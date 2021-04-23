package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceBank;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardRequirementsTest {
    LeaderCardRequirements leaderCardRequirements;

    @RepeatedTest(3)
    @DisplayName("Test getRequiredDevelopmentCards")
    void getRequiredDevelopmentCardsTest(RepetitionInfo repetitionInfo) {
        if (repetitionInfo.getCurrentRepetition() == 0) {
            Map<Color, LeaderCardRequirements.LevelQuantityPair> developmentCardsRequirement = new HashMap<>();
            developmentCardsRequirement.put(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL2, 1));
            leaderCardRequirements = new LeaderCardRequirements(developmentCardsRequirement, null);

            LeaderCardRequirements.LevelQuantityPair actualLevelQuantityPair =leaderCardRequirements.getRequiredDevelopmentCards()
                    .entrySet()
                    .stream()
                    .filter(
                            colorLevelQuantityPairEntry -> colorLevelQuantityPairEntry.getKey().equals(Color.BLUE)
                    ).findFirst()
                    .get()
                    .getValue();
            Level actualLevel = actualLevelQuantityPair.getLevel();
            int actualQuantity = actualLevelQuantityPair.getQuantity();

            assertAll(
                    () -> assertTrue(actualLevel.equals(Level.LEVEL2), "Error: LeaderCardRequirements returned a different level than the one passed"),
                    () -> assertTrue(actualQuantity == 1, "Error: LeaderCardRequirements returned a different quantity than the one passed"),
                    () -> assertTrue(leaderCardRequirements.getRequiredDevelopmentCards().size()==1, "Error: LeaderCardRequirements contains more values than it should")
            );
        } else if (repetitionInfo.getCurrentRepetition() == 1){
            Map<Color, LeaderCardRequirements.LevelQuantityPair> developmentCardsRequirement = new HashMap<>();
            developmentCardsRequirement.put(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 2));
            developmentCardsRequirement.put(Color.YELLOW, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1));
            leaderCardRequirements = new LeaderCardRequirements(developmentCardsRequirement, null);
            int blueQuantity = leaderCardRequirements.getRequiredDevelopmentCards()
                    .entrySet()
                    .stream()
                    .filter(
                        colorLevelQuantityPairEntry -> colorLevelQuantityPairEntry.getKey().equals(Color.BLUE)
                    ).findFirst()
                    .get()
                    .getValue()
                    .getQuantity();
            int yellowQuantity = leaderCardRequirements.getRequiredDevelopmentCards()
                    .entrySet()
                    .stream()
                    .filter(
                            colorLevelQuantityPairEntry -> colorLevelQuantityPairEntry.getKey().equals(Color.YELLOW)
                    ).findFirst()
                    .get()
                    .getValue()
                    .getQuantity();

            assertAll(
                    () -> assertTrue(blueQuantity == 2, "Error: LeaderCardRequirements returned a different quantity for the first passed parameter"),
                    () -> assertTrue(yellowQuantity ==1, "Error: LeaderCardRequirements returned a different quantity for the second passed parameter"),
                    () -> assertTrue(leaderCardRequirements.getRequiredDevelopmentCards().size()==2, "Error: LeaderCardRequirements contains more values than it should")
            );
        }else{
            Map<Color, LeaderCardRequirements.LevelQuantityPair> developmentCardsRequirement = new HashMap<>();
            developmentCardsRequirement.put(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1));
            developmentCardsRequirement.put(Color.YELLOW, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1));
            leaderCardRequirements = new LeaderCardRequirements(developmentCardsRequirement, null);
            int blueQuantity = leaderCardRequirements.getRequiredDevelopmentCards()
                    .entrySet()
                    .stream()
                    .filter(
                            colorLevelQuantityPairEntry -> colorLevelQuantityPairEntry.getKey().equals(Color.BLUE)
                    ).findFirst()
                    .get()
                    .getValue()
                    .getQuantity();
            int yellowQuantity = leaderCardRequirements.getRequiredDevelopmentCards()
                    .entrySet()
                    .stream()
                    .filter(
                            colorLevelQuantityPairEntry -> colorLevelQuantityPairEntry.getKey().equals(Color.YELLOW)
                    ).findFirst()
                    .get()
                    .getValue()
                    .getQuantity();

            assertAll(
                    () -> assertTrue(blueQuantity == 1, "Error: LeaderCardRequirements returned a different quantity for the first passed parameter"),
                    () -> assertTrue(yellowQuantity ==1, "Error: LeaderCardRequirements returned a different quantity for the second passed parameter"),
                    () -> assertTrue(leaderCardRequirements.getRequiredDevelopmentCards().size()==2, "Error: LeaderCardRequirements contains more values than it should")
            );
            }
    }

    @Test
    @DisplayName("test setRequiredResources and getRequiredResources")
    void setRequiredResourcesAndGetRequiredResourcesTest() {
        Map<Resource, Integer> expectedRequiredResources = ResourceBank.getResource(Resource.COIN);
        expectedRequiredResources.put(Resource.COIN, 5);
        leaderCardRequirements = new LeaderCardRequirements(null, expectedRequiredResources);
        Map<Resource, Integer> actualRequiredResources = leaderCardRequirements.getRequiredResources();
        assertEquals(expectedRequiredResources, actualRequiredResources, "Error: the class returned set of resource requirements which is different than the one used in the setter");
    }

    @Nested
    @DisplayName("getRequiredResources method tests")
    class getRequiredResourcesTests {
        Map<Resource, Integer> requiredResources;

        @BeforeEach
        void init() {
            Map<Resource, Integer> expectedRequiredResources = ResourceBank.getResource(Resource.COIN);
            expectedRequiredResources.put(Resource.COIN, 5);
            leaderCardRequirements = new LeaderCardRequirements(null, expectedRequiredResources);
            requiredResources = leaderCardRequirements.getRequiredResources();
        }

        @Test
        @DisplayName("getRequiredResources safety test")
        void getRequiredResourcesSafetyTest() {
            assertNotSame(leaderCardRequirements.getRequiredResources(), leaderCardRequirements.getRequiredResources());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(requiredResources);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(leaderCardRequirements.getRequiredResources(), leaderCardRequirements.getRequiredResources());
        }

        @Test
        @DisplayName("Returns correctly sized map")
        void sizeTest() {
            assertEquals(leaderCardRequirements.getRequiredResources().size(), 1);
        }
    }

    @Nested
    @DisplayName("getRequiredDevelopmentCards method tests")
    class getRequiredDevelopmentCardsTests {
        Map<Color, LeaderCardRequirements.LevelQuantityPair> requiredDevelopmentCards;

        @BeforeEach
        void init() {
            Map<Color, LeaderCardRequirements.LevelQuantityPair> developmentCardsRequirement = new HashMap<>();
            developmentCardsRequirement.put(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL2, 1));
            leaderCardRequirements = new LeaderCardRequirements(developmentCardsRequirement, null);
            requiredDevelopmentCards = leaderCardRequirements.getRequiredDevelopmentCards();
        }

        @Test
        @DisplayName("getRequiredDevelopmentCards safety test")
        void getRequiredDevelopmentCardsSafetyTest() {
            assertNotSame(leaderCardRequirements.getRequiredDevelopmentCards(), leaderCardRequirements.getRequiredDevelopmentCards());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(requiredDevelopmentCards);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(leaderCardRequirements.getRequiredDevelopmentCards(), leaderCardRequirements.getRequiredDevelopmentCards());
        }
    }
}