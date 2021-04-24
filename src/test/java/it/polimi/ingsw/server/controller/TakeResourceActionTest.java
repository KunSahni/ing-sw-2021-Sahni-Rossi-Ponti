package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.message.action.TakeResourceAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.market.Market;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@Disabled
public class TakeResourceActionTest {
    Player player;

    @BeforeEach
    void init() {
        Game game = new Game(1, 2);
        player = new Player("Nick", game);
        Turn turn = new Turn(game, player);
    }

    @ParameterizedTest(name = "Selected a {0}")
    @DisplayName("Correct increase of resources in the temporary storage")
    @CsvSource({"Row, true, 4", "Column, false, 3"})
    void correctResourceIncreaseTest(String ignore, boolean choseRowFlag, int expectedStored) {
    }
}
