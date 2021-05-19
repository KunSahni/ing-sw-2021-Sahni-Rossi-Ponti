package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.GameState;

/**
 * This class contains an update regarding the state of the game which will be saved in the local DumbModel
 */
public class GameStateUpdate extends BroadcastRenderable {
    private final GameState updatedGameState;

    public GameStateUpdate(Game game) {
        this.updatedGameState = game.getCurrentState();
    }

    @Override
    public void render(UI ui) {
        ui.updateGameState(updatedGameState);
    }
}
