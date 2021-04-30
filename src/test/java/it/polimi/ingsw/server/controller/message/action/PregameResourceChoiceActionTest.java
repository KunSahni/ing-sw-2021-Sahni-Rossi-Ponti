package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.playeraction.PregameResourceChoiceAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PregameResourceChoiceActionTest {
    PersonalBoard personalBoard;
    PregameResourceChoiceAction pregameResourceChoiceAction;

    @BeforeEach
    void init() {
        Game game = new Game(1, 1);
        Player player = new Player("Nick", game);
        personalBoard = new PersonalBoard(player);
        pregameResourceChoiceAction = new PregameResourceChoiceAction(personalBoard,
                new HashMap<>() {{
                    put(Resource.COIN, 2);
                }});
    }

    @Test
    @DisplayName("Number of resources in the board increased correctly")
    void correctResourceIncreaseTest() {
        int initialResourceCount = personalBoard.getResourceCount();
        pregameResourceChoiceAction.execute();
        assertEquals(initialResourceCount + 2, personalBoard.getResourceCount());
    }

    @Test
    @DisplayName("The forwarded resources are now contained in the board")
    void correctBoardResourceContent() {
        pregameResourceChoiceAction.execute();
        assertTrue(personalBoard.containsResources(new HashMap<>() {{put(Resource.COIN, 2);}}));
    }
}
