package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.ProductionCombo;
import it.polimi.ingsw.server.controller.message.action.playeraction.ActivateProductionAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateProductionActionTest {
    ActivateProductionAction activateProductionAction;
    PersonalBoard board;
    ProductionCombo productionCombo;
    Player player;
    Game game;

    @BeforeEach
    void init(){
        productionCombo = new ProductionCombo();
        game = new Game(1, 1);
        player = new Player("Nick", game);
        board = new PersonalBoard(player);
    }

    @Nested
    @DisplayName("All resources are added correctly")
    class AddedResourceCorrectlyTest {
        @Test
        void AddedResourcesByLeaderCard(){
            board.storeInDepots(Map.of(Resource.COIN, 1));
            productionCombo.setLeaderCards(List.of(new ProduceLeaderCard(3, null, Resource.COIN, 1)));
            productionCombo.setDiscardedResourcesFromDepots(Map.of(Resource.COIN, 1));
                int initialFaithTrackPosition = board.getFaithTrack().getFaithMarkerPosition();
            activateProductionAction = new ActivateProductionAction(board, productionCombo);
            activateProductionAction.execute();
            assertAll(
                    ()->assertEquals(initialFaithTrackPosition+1, board.getFaithTrack().getFaithMarkerPosition()),
                    ()->assertFalse(board.containsResources(Map.of(Resource.COIN, 1)))
            );
        }
    }
}
