package model.leadercard;

import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Generic LeaderCard tests")
public class LeaderCardTest {
    LeaderCard testCard;

    @BeforeEach
    void init() {
        testCard = new ConvertLeaderCard(1, new LeaderCardRequirements(), Resource.COIN);
    }

    @Test
    @DisplayName("getLeaderCardRequirements safety test")
    void getLeaderCardRequirementsSafetyTest() {
        assertNotSame(testCard.getLeaderCardRequirements(), testCard.getLeaderCardRequirements());
    }

    @Test
    @DisplayName("activate test")
    void activationTest() {
        boolean initialState = testCard.isActive();
        testCard.activate();
        assertAll(
                () -> assertFalse(initialState),
                () -> assertTrue(testCard.isActive())
        );
    }
}