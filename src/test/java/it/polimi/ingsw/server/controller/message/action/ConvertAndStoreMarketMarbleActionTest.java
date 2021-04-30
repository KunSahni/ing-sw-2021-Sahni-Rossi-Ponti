package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.gameaction.ConvertAndStoreMarketMarbleAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests behavior of a request of conversion from MarketMarbles
 * to Resource from the bank
 */
public class ConvertAndStoreMarketMarbleActionTest {
    PersonalBoard personalBoard;
    ConvertAndStoreMarketMarbleAction convertAndStoreMarketMarbleAction;

    @BeforeEach
    void init() {
        Game game = new Game(1, 1);
        Player player = new Player("Nick", game);
        personalBoard = new PersonalBoard(player);
        Map<MarketMarble, Integer> marbles = new HashMap<>() {{
            put(MarketMarble.BLUE, 1);
            put(MarketMarble.PURPLE, 1);
            put(MarketMarble.RED, 1);
        }};
        convertAndStoreMarketMarbleAction = new ConvertAndStoreMarketMarbleAction(personalBoard, marbles);
    }

    @Test
    @DisplayName("Correct resource increase in storages")
    void correctResourceIncreaseTest() {
        int initialCount = personalBoard.getResourceCount();
        convertAndStoreMarketMarbleAction.execute();
        assertEquals(initialCount + 2, personalBoard.getResourceCount());
    }

    @Test
    @DisplayName("Correct Faith Marker movement")
    void correctFaithMarkerMovementTest() {
        int initialPosition = personalBoard.getFaithTrack().getFaithMarkerPosition();
        convertAndStoreMarketMarbleAction.execute();
        assertEquals(initialPosition + 1, personalBoard.getFaithTrack().getFaithMarkerPosition());
    }

    @Test
    @DisplayName("Forwarded resources get added to storages")
    void correctResourceStorageTest() {
        Map<Resource, Integer> contentChecked = new HashMap<>() {{
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 1);
        }};
        boolean initiallyContained = personalBoard.containsResources(contentChecked);
        convertAndStoreMarketMarbleAction.execute();
        assertAll(
                () -> assertFalse(initiallyContained),
                () -> assertTrue(personalBoard.containsResources(contentChecked))
        );
    }
}
