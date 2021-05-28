package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardTest {
    DevelopmentCard card;
    Game game;

    @BeforeEach
    void init() throws IOException {
        game = new Game(new Server(), 1, new ArrayList<>());
        card = game.getDevelopmentCardsBoard().peekBoard()[2][1].peek();
    }

    @AfterEach
    void tearDown() {
        new ChangesHandler(1).publishGameOutcome(game);
    }

    @Nested
    @DisplayName("Safety tests")
    class SafetyTests {
        @Test
        @DisplayName("getInputResources safety test")
        void getInputResourcesSafetyTest() {
            assertNotSame(card.getInputResources(), card.getInputResources());
        }

        @Test
        @DisplayName("getOutputResources safety test")
        void getOutputResourcesSafetyTest() {
            assertNotSame(card.getOutputResources(), card.getOutputResources());
        }
        //todo: outputResources in DevelopmentCard non viene settato, tutto il resto si

        @Test
        @DisplayName("getCost safety test")
        void getCostSafetyTest() {
            assertNotSame(card.getCost(), card.getCost());
        }
    }

    /**
     * Generated ProductionOutput must match card details
     */
    @Test
    @DisplayName("produce's ProductionOutput matches card details")
    void produceTest() {
        ProductionOutput productionOutput = card.produce();
        assertAll(
                () -> assertEquals(card.getOutputResources(), productionOutput.getResources()),
                () -> assertEquals(card.getFaithIncrement(), productionOutput.getFaithIncrement())
        );
    }
}
