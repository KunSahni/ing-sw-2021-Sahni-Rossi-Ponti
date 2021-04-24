package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.ResourcePregameAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ResourcePregameActionTest {
    PersonalBoard personalBoard;
    ResourcePregameAction resourcePregameAction;

    @BeforeEach
    void init() {
        Game game = new Game(1, 1);
        Player player = new Player("Nick", game);
        personalBoard = new PersonalBoard(player);
        resourcePregameAction = new ResourcePregameAction(personalBoard,
                new HashMap<>() {{
                    put(Resource.COIN, 2);
                }});
    }

    @Test
    @DisplayName("Number of resources in the board increased correctly")
    void correctResourceIncreaseTest() {
        int initialResourceCount = personalBoard.getResourceCount();
        resourcePregameAction.forward();
        assertEquals(initialResourceCount + 2, personalBoard.getResourceCount());
    }

    @Test
    @DisplayName("The forwarded resources are now contained in the board")
    void correctBoardResourceContent() {
        resourcePregameAction.forward();
        assertTrue(personalBoard.containsResources(new HashMap<>() {{put(Resource.COIN, 2);}}));
    }
}
