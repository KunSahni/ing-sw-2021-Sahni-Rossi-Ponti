package it.polimi.ingsw.server.model.leadercard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Generic LeaderCard tests")
public class LeaderCardTest {
    LeaderCard leaderCard;
    int victoryPoints;
    LeaderCardRequirements leaderCardRequirements;

    @BeforeEach
    void init() {
        victoryPoints = 1;
        leaderCardRequirements = new LeaderCardRequirements(null, null);
        leaderCard = new ConvertLeaderCard(victoryPoints, leaderCardRequirements, Resource.COIN);
    }

    @Test
    @DisplayName("getLeaderCardRequirements safety test")
    void getLeaderCardRequirementsSafetyTest() {
        assertNotSame(leaderCard.getLeaderCardRequirements(), leaderCard.getLeaderCardRequirements());
    }

    @Test
    @DisplayName("getLeaderCardRequirements test")
    void getLeaderCardRequirementsTest() {
        assertEquals(leaderCard.getLeaderCardRequirements(), leaderCardRequirements);
    }

    @Test
    @DisplayName("getVictoryPoints test")
    void getVictoryPointsTest() {
        assertEquals(leaderCard.getVictoryPoints(), victoryPoints);
    }

    @Test
    @DisplayName("getAbility test")
    void getAbilityTest() {
        assertEquals(leaderCard.getAbility(), LeaderCardAbility.CONVERT);
    }


    @Test
    @DisplayName("activate test")
    void activationTest() {
        boolean initialState = leaderCard.isActive();
        leaderCard.activate();
        assertAll(
                () -> assertFalse(initialState),
                () -> assertTrue(leaderCard.isActive())
        );
    }


    @Test
    @DisplayName("convertToDumb method test")
    void convertToDumbTest(){
        DumbStoreLeaderCard dumbStoreLeaderCard = (DumbStoreLeaderCard) leaderCard.convertToDumb();
        assertAll(
                ()-> assertEquals(leaderCard.getAbility(), dumbStoreLeaderCard.getAbility(),"Error: dumb leader card has a different ability than the real leader card"),
                ()-> assertEquals(leaderCard.getVictoryPoints(), dumbStoreLeaderCard.getVictoryPoints(),"Error: dumb leader card has different victory points than the real leader card"),
                ()-> assertEquals(leaderCard.getLeaderCardRequirements(), dumbStoreLeaderCard.getLeaderCardRequirements(),"Error: dumb leader card has different requirements than the real leader card")
        );
    }
}