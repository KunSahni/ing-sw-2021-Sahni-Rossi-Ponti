package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.Game;

/**
 *  This class contains an regarding the outcome of a single player game
 */
public class SinglePlayerGameOutcomeUpdate extends BroadcastRenderable {
    private final int finalScore;
    //private final boolean won; needed?

    public SinglePlayerGameOutcomeUpdate(Game game) {
        finalScore = game.getPlayerList().get(0).getPersonalBoard().getVictoryPoints();

    }

    @Override
    public void render(UI ui) {
        ui.renderGameOutcome(finalScore);
    }
}
