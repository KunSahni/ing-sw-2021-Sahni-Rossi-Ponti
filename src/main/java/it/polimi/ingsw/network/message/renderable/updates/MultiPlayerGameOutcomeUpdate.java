package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.Comparator;
import java.util.TreeMap;

/**
 *  This class contains an regarding the outcome of a multiplayer game
 */
public class MultiPlayerGameOutcomeUpdate extends BroadcastRenderable {
    private final TreeMap<Integer, String> finalScores;

    public MultiPlayerGameOutcomeUpdate(Game game) {
        Comparator<Player> c = Comparator.comparingInt(Player::getPosition);
        finalScores = new TreeMap<>();
        for(Player player: game.getPlayerList())
            finalScores.put(player.getPersonalBoard().getVictoryPoints(), player.getNickname());
    }

    @Override
    public void render(UI ui) {
        ui.renderGameOutcome(finalScores);
    }
}
