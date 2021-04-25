package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.gamepackage.ProductionCombo;
import it.polimi.ingsw.server.model.leadercard.*;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionComboTest {
    ProductionCombo productionCombo;

    @BeforeEach
    void init(){
        productionCombo = new ProductionCombo();
    }

    @Test
    void setDevelopmentCardSlotsTest(){
        DevelopmentCardSlot developmentCardSlot1 = new DevelopmentCardSlot();
        DevelopmentCardSlot developmentCardSlot2 = new DevelopmentCardSlot();
        List<DevelopmentCardSlot> developmentCardSlots = new ArrayList<>();

        developmentCardSlots.add(developmentCardSlot1);
        developmentCardSlots.add(developmentCardSlot2);
        productionCombo.setDevelopmentCardSlots(developmentCardSlots);
        assertEquals(productionCombo.getDevelopmentCardSlots(), developmentCardSlots);
    }

    @Test
    @DisplayName("DefaultSlotOutput has been set")
    void setDefaultSlotOutputTest(){
        Map<Resource, Integer> expected = new HashMap<>();

        expected.put(Resource.COIN, 3);
        expected.put(Resource.SERVANT, 2);
        productionCombo.setDefaultSlotOutput(expected);

        assertEquals(expected, productionCombo.getDefaultSlotOutput());
    }

    @Test
    @DisplayName("LeaderCards has been set")
    void setLeaderCardsTest(){
        List<LeaderCard> leaderCards = new ArrayList<>();

        leaderCards.add(new ConvertLeaderCard(3, new LeaderCardRequirements(null, null), Resource.COIN));
        leaderCards.add(new DiscountLeaderCard(5, new LeaderCardRequirements(null, null), Resource.SHIELD));

        productionCombo.setLeaderCards(leaderCards);
        assertEquals(productionCombo.getLeaderCards(), leaderCards);
    }

    @Test
    @DisplayName("LeaderCardOutput has been set")
    void setLeaderCardOutputsTest(){
        Map<LeaderCard, Resource> leaderCards = new HashMap();

        leaderCards.put(new ProduceLeaderCard(1, new LeaderCardRequirements(null, null), Resource.STONE, 2), Resource.SERVANT);
        leaderCards.put(new ConvertLeaderCard(2, new LeaderCardRequirements(null, null), Resource.SERVANT), Resource.SHIELD);

        productionCombo.setLeaderCardOutputs(leaderCards);
        assertEquals(productionCombo.getLeaderCardOutputs(), leaderCards);
    }

    @Test
    @DisplayName("DiscardedResource from depots has been set")
    void setDiscardedResourcesFromDepotsTest(){
        Map<Resource, Integer> discardedResources = Map.of(Resource.STONE, 1);

        productionCombo.setDiscardedResourcesFromDepots(discardedResources);
        assertEquals(productionCombo.getDiscardedResourcesFromDepots(), discardedResources);
    }

    @Test
    @DisplayName("DiscardedResource from strongbox has been set")
    void setDiscardedResourcesFromStrongboxTest(){
        Map<Resource, Integer> discardedResources = Map.of(Resource.STONE, 1);

        productionCombo.setDiscardedResourcesFromStrongbox(discardedResources);
        assertEquals(productionCombo.getDiscardedResourcesFromStrongbox(), discardedResources);
    }
}
