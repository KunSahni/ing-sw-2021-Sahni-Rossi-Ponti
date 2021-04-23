package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardSlotTest {
    DevelopmentCardSlot developmentCardSlot;
    List<DevelopmentCard> developmentCards;

    @BeforeEach
    void setUp(){
        developmentCardSlot = new DevelopmentCardSlot();
        developmentCards = new ArrayList<>();
        DevelopmentCardsBoard developmentCardsBoard = new DevelopmentCardsBoard();

        //Add the DevelopmentCards to a List
        developmentCards.add(developmentCardsBoard.pick(Level.LEVEL1, Color.BLUE));
        developmentCards.add(developmentCardsBoard.pick(Level.LEVEL2, Color.GREEN));
        developmentCards.add(developmentCardsBoard.pick(Level.LEVEL3, Color.PURPLE));

        //Place the DevelopmentCards inside the DevelopmentCardSlot
        developmentCards.forEach(
                developmentCard -> developmentCardSlot.placeCard(developmentCard)
        );
    }

    @Test
    @DisplayName("Test placeDevelopmentCards")
    void placeAndGetDevelopmentCardsTest() {
        assertEquals(developmentCards, developmentCardSlot.getDevelopmentCards(), "Error: was expecting " + developmentCards + ", but received " + developmentCardSlot.getDevelopmentCards());
    }

    @Test
    @DisplayName("Test peek")
    void peekTest() {
        DevelopmentCard expectedCard = developmentCards.get(developmentCards.size()-1);
        DevelopmentCard peekedCard = developmentCardSlot.peek();
        assertEquals(expectedCard, peekedCard, "Error: was expecting " + expectedCard + ", but received " + peekedCard);
    }

    @Test
    @DisplayName("Test produce")
    void produceTest() {
        ProductionOutput expectedProductionOutput = developmentCardSlot.peek().produce();
        ProductionOutput actualProductionOutput = developmentCardSlot.produce();
        assertEquals(expectedProductionOutput, actualProductionOutput, "Error: was expecting " + expectedProductionOutput + ", but received " + actualProductionOutput);
    }

    @Test
    @DisplayName("Test getVictoryPoints")
    void getVictoryPointsTest() {
        int expectedVictoryPoints = developmentCards.stream().mapToInt(
               DevelopmentCard::getVictoryPoints
        ).sum();
        int actualVictoryPoints = developmentCardSlot.getVictoryPoints();
        assertEquals(expectedVictoryPoints, actualVictoryPoints, "Error: was expecting " + expectedVictoryPoints + ", but received " + actualVictoryPoints);
    }

    @Test
    @DisplayName("Test getCardsNumber")
    void getCardsNumberTest() {
        int expectedNumber = developmentCards.size();
        int actualNumber = developmentCardSlot.getCardsNumber();
        assertEquals(expectedNumber, actualNumber, "Error: was expecting " + expectedNumber + ", but received " + actualNumber);

    }

    @Nested
    @DisplayName("getDevelopmentCards method tests")
    class GetPopesFavorsTests {
        List<DevelopmentCard> developmentCards;

        @BeforeEach
        void init(){
            developmentCards = developmentCardSlot.getDevelopmentCards();
        }

        @Test
        @DisplayName("getDevelopmentCards safety test")
        void getDevelopmentCardsSafetyTest() {
            assertNotSame(developmentCardSlot.getDevelopmentCards(), developmentCardSlot.getDevelopmentCards());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(developmentCards);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(developmentCardSlot.getDevelopmentCards(), developmentCardSlot.getDevelopmentCards());
        }

        @Test
        @DisplayName("Returns correctly sized list")
        void sizeTest() {
            assertTrue(developmentCardSlot.getDevelopmentCards().size()<=3 && developmentCardSlot.getDevelopmentCards().size()>=0);
        }
    }
}