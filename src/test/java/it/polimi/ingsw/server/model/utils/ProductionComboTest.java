package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.leadercard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
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
    @DisplayName("setDevelopmentCards method test")
    void setDevelopmentCardsTest() throws FileNotFoundException {
        ChangesHandler changesHandler = new ChangesHandler(1);
        DevelopmentCard developmentCard1 = changesHandler.readDevelopmentCardsBoard().peekCard(Level.LEVEL1, Color.GREEN);
        DevelopmentCard developmentCard2 = changesHandler.readDevelopmentCardsBoard().peekCard(Level.LEVEL2, Color.BLUE);
        List<DumbDevelopmentCard> developmentCardSlots = new ArrayList<>();

        developmentCardSlots.add(new DumbDevelopmentCard(developmentCard1));
        developmentCardSlots.add(new DumbDevelopmentCard(developmentCard2));
        productionCombo.setDevelopmentCards(developmentCardSlots);
        assertEquals(productionCombo.getDevelopmentCards(), developmentCardSlots);
    }

    @Test
    @DisplayName("setDefaultSlotOutput method test")
    void setDefaultSlotOutputTest(){
        Map<Resource, Integer> expected = new HashMap<>();

        expected.put(Resource.COIN, 3);
        expected.put(Resource.SERVANT, 2);
        productionCombo.setDefaultSlotOutput(expected);

        assertEquals(expected, productionCombo.getDefaultSlotOutput());
    }

    @Test
    @DisplayName("setLeaderCardProduction method test")
    void setLeaderCardProductionTest(){
        Map<DumbProduceLeaderCard, Resource> leaderCardsProduction = new HashMap<>();
        leaderCardsProduction.put(new DumbProduceLeaderCard(new ProduceLeaderCard(3,new LeaderCardRequirements(null, null), Resource.COIN, 1)), Resource.SERVANT);
        leaderCardsProduction.put(new DumbProduceLeaderCard(new ProduceLeaderCard(5,new LeaderCardRequirements(null, null), Resource.SHIELD, 1)), Resource.STONE);

        productionCombo.setLeaderCardProduction(leaderCardsProduction);
        assertEquals(productionCombo.getLeaderCardProduction(), leaderCardsProduction);
    }
    @Test
    @DisplayName("setDiscardedResourcesFromDepots method test")
    void setDiscardedResourcesFromDepotsTest(){
        Map<Resource, Integer> discardedResources = Map.of(Resource.STONE, 1);

        productionCombo.setDiscardedResourcesFromDepots(discardedResources);
        assertEquals(productionCombo.getDiscardedResourcesFromDepots(), discardedResources);
    }

    @Test
    @DisplayName("setDiscardedResourcesFromStrongbox method test")
    void setDiscardedResourcesFromStrongboxTest(){
        Map<Resource, Integer> discardedResources = Map.of(Resource.STONE, 1);

        productionCombo.setDiscardedResourcesFromStrongbox(discardedResources);
        assertEquals(productionCombo.getDiscardedResourcesFromStrongbox(), discardedResources);
    }
}
